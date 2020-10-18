package com.datuanyuan.initdata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.datuanyuan.bean.SearchMerchantsBean;
import com.datuanyuan.util.CsvUtil;
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
 * 初始化搜索引擎数据工具类-初始化商家
 *
 * @author weiyuan
 * @version 1.0
 */
public class BatchImportMerchants {

    private Log logger = LogFactory.getLog(BatchImportMerchants.class);

    private RestHighLevelClient client;

    private String host = "es-cn-0pp16fa950009sfkp.public.elasticsearch.aliyuncs.com";
    private String username = "elastic";
    private String password = "1578fhtx46rt@";
    private String aliasename = "search_merchants";
    private String typeName = "merchants";

    private int port = 9200;
    private int batchImportSize = 100000;// 一个批次的最大数据量

    public BatchImportMerchants() {
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
     * 批量添加商家信息
     *
     * @param aliasName
     * @param typeName
     * @param totalList
     * @return
     * @throws IOException
     */
    private boolean addBatch(String aliasName, String typeName, int begin, int end, List<SearchMerchantsBean> totalList)
            throws IOException {
        List<SearchMerchantsBean> postList = totalList.subList(begin, end);
        BulkRequest bulkRequest = new BulkRequest();
        for (SearchMerchantsBean d : postList) {
            String jsonString = JSON.toJSONString(d);
            IndexRequest indexRequest = new IndexRequest(aliasName, typeName, String.valueOf(d.getMerchants_id()));
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(10));
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            logger.error("数据批量添加失败！，失败信息：" + bulkResponse.buildFailureMessage() + ", status: "
                    + bulkResponse.status().getStatus());
        } else {
            logger.info("数据批量添加成功！");
            return true;
        }
        return false;
    }

    public void importdata(String path) {
        List<SearchMerchantsBean> list = Lists.newArrayList();
        File file = new File(path);
        List<List<String>> totalList = CsvUtil.readCSV(file);
        for (int i = 1; i < totalList.size(); i++) {
            List<String> rowList = totalList.get(i);
            SearchMerchantsBean searchMerchantsBean = new SearchMerchantsBean();
            searchMerchantsBean.setMerchants_id(rowList.get(0));// 商家代理商实验室id
            searchMerchantsBean.setMerchants_name(rowList.get(1));// 商家名称
            searchMerchantsBean.setMerchants_name_ik(rowList.get(1));//商家名称
            searchMerchantsBean.setMerchants_name_smart(rowList.get(1));//商家名称
            searchMerchantsBean.setMerchants_name_standard(rowList.get(1));//商家名称
            searchMerchantsBean.setMerchants_logo(rowList.get(2));// 商家照片
            searchMerchantsBean.setMerchants_total_sales_num(StringHelper.isNotEmpty(rowList.get(3)) ? Long.parseLong(rowList.get(3)) : 0l);// 商家总销量
            searchMerchantsBean.setMerchants_on_saling_goods_num(StringHelper.isNotEmpty(rowList.get(4)) ? Long.parseLong(rowList.get(5)) : 0l);// 商家在售商品数量
            searchMerchantsBean.setMerchants_service_feature(rowList.get(5));// 商家特色服务
            searchMerchantsBean.setMerchants_province(rowList.get(6));// 商家所在省
            searchMerchantsBean.setMerchants_status(rowList.get(7));// 商家状态0:下线1:上线
            list.add(searchMerchantsBean);
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
    private void batchPost(int size, int begin, List<SearchMerchantsBean> list) throws IOException {
        if (size > batchImportSize) {
            addBatch(aliasename, typeName, begin, begin + batchImportSize, list);
            list = list.subList(begin + batchImportSize, list.size());
            batchPost(list.size(), 0, list);
        } else {
            addBatch(aliasename, typeName, begin, list.size(), list);
        }
    }

    public static void main(String[] args) throws IOException {
        BatchImportMerchants batchImportData = new BatchImportMerchants();
        batchImportData.importdata("D://1importgoods//merchants.csv");
    }
}
