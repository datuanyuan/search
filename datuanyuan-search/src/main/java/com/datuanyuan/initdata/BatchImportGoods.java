package com.datuanyuan.initdata;

import java.io.File;
import java.io.IOException;
import java.util.List;


import com.datuanyuan.bean.SearchGoodsBean;
import com.datuanyuan.util.CsvUtil;
import com.datuanyuan.util.MoneyUtil;
import com.datuanyuan.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

/**
 * 初始化搜索引擎数据工具类-初始化商品
 *
 * @author weiyuan
 * @version 1.0
 */
public class BatchImportGoods {

    private Log logger = LogFactory.getLog(BatchImportGoods.class);

    private RestHighLevelClient client;

    private String host = "es-cn-0pp16fa950009sfkp.public.elasticsearch.aliyuncs.com";
    private String username = "elastic";
    private String password = "1578fhtx46rt@";
    private String aliasename = "index_goods_20191230-1";
    private String typeName = "goods";

    private int port = 9200;
    private int batchImportSize = 40000;// 一个批次的最大数据量

    public BatchImportGoods() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port)).setHttpClientConfigCallback(
                new RestClientBuilder.HttpClientConfigCallback() {
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                }));
    }

    /**
     * 批量添加DetectionInfo信息
     *
     * @param aliasName
     * @param typeName
     * @param totalList
     * @return
     * @throws IOException
     */
    private boolean addBatch(String aliasName, String typeName, int begin, int end, List<SearchGoodsBean> totalList)
            throws IOException {
        List<SearchGoodsBean> postList = totalList.subList(begin, end);
        BulkRequest bulkRequest = new BulkRequest();
        for (SearchGoodsBean d : postList) {
            String jsonString = JSON.toJSONString(d);
            IndexRequest indexRequest = new IndexRequest(aliasName, typeName, String.valueOf(d.getGoods_id()));
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(40));
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            logger.error("数据批量添加失败！，失败信息：" + bulkResponse.buildFailureMessage() + ", status: "
                    + bulkResponse.status().getStatus());
        } else {
            logger.error("数据批量添加成功！");
            return true;
        }
        return false;
    }

    public void importdata(String path) {
        List<SearchGoodsBean> list = Lists.newArrayList();
        File file = new File(path);
        List<List<String>> totalList = CsvUtil.readCSV(file);
        for (int i = 1; i < totalList.size(); i++) {
            try {
                List<String> rowList = totalList.get(i);
                SearchGoodsBean searchGoodsBean = new SearchGoodsBean();
                searchGoodsBean.setMerchants_id(rowList.get(0));// 商家代理商实验室id
                searchGoodsBean.setMerchants_name(rowList.get(1));// 商家名称
                searchGoodsBean.setMerchants_name_ik(rowList.get(1));
                searchGoodsBean.setGoods_class_id(rowList.get(2));// 分类id
                searchGoodsBean.setGoods_class_name(rowList.get(3));// 分类名称
                searchGoodsBean.setGoods_id(rowList.get(4));//
                searchGoodsBean.setGoods_name(rowList.get(5));// 商品名称
                searchGoodsBean.setGoods_name_smart(rowList.get(5));//
                searchGoodsBean.setGoods_name_standard(rowList.get(5));
                searchGoodsBean.setGoods_name_ik(rowList.get(5));
                searchGoodsBean.setGoods_img(rowList.get(6));//
                searchGoodsBean.setGoods_price(StringHelper.isNotEmpty(rowList.get(7)) ? MoneyUtil.convertYuanToFen(rowList.get(7)) : 0l);
                searchGoodsBean.setGoods_status(rowList.get(8));// 0,1,2,3上架,放入仓库,审核中,审核失败4,审核通过定时商品,
                searchGoodsBean.setGoods_sales(StringHelper.isNotEmpty(rowList.get(9)) ? Long.parseLong(rowList.get(9)) : 0l);// 商品总销量
                searchGoodsBean.setGoods_num(StringHelper.isNotEmpty(rowList.get(10)) ? Integer.parseInt(rowList.get(10)) : 0);
                list.add(searchGoodsBean);
            } catch (Exception e) {
                System.out.println("添加商品报错，报错信息： " + e.getMessage() + " ,报错行数： " + i);
                e.printStackTrace();
            }
        }
        System.out.println("总数据量： " + list.size());
        try {
            batchPost(list.size(), 0, list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("总数据添加结束！！");
    }

    /**
     * 批量发送数据
     *
     * @param size
     *            要发送的总数据量
     * @param begin
     *            发送数据起始index
     * @param list
     *            要发送的总数据list
     */
    private void batchPost(int size, int begin, List<SearchGoodsBean> list) throws IOException {
        if (size > batchImportSize) {
            addBatch(aliasename, typeName, begin, begin + batchImportSize, list);
            list = list.subList(begin + batchImportSize, list.size());
            batchPost(list.size(), 0, list);
        } else {
            addBatch(aliasename, typeName, begin, list.size(), list);
        }
    }

    public static void main(String[] args) throws IOException {
        BatchImportGoods batchImportData = new BatchImportGoods();
        batchImportData.importdata("D://1importgoods//goods.csv");
    }
}
