package com.datuanyuan.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.datuanyuan.bean.*;
import com.datuanyuan.service.ElasticsearchService;
import com.datuanyuan.service.SearchService;
import com.datuanyuan.util.CollectionHelp;
import com.datuanyuan.util.StringHelper;
import io.micrometer.core.instrument.util.JsonUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    public RestHighLevelClient client;

    @Autowired
    public ElasticsearchService elasticsearchService;

    @Value("${search.goods.alias}")
    private String searchGoodsAlias;

    @Value("${search.goods.type}")
    private String searchGoodsType;

    @Value("${search.goods.fields}")
    private String searchGoodsField;

    @Override
    public SearchResponseBean searchKeyWordPopup(String aliasName, String type, String searchFields, SearchRequestBean sbean) {
        SearchResponseBean searchResponseBean = new SearchResponseBean();
        if (StringHelper.isEmpty(sbean.getSearchKey())) {
            return searchResponseBean;
        }
        Map<String, Float> fields = getFieldsMap(searchFields);
        MultiSearchRequest multiRequest = new MultiSearchRequest();
        for (String fieldName : fields.keySet()) {
            SearchRequest searchRequest = new SearchRequest(aliasName);
            searchRequest.types(type);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MatchPhrasePrefixQueryBuilder prefixQuery = QueryBuilders.matchPhrasePrefixQuery(fieldName, sbean.getSearchKey());
            prefixQuery.boost(fields.get(fieldName));
            prefixQuery.maxExpansions(5);
            searchSourceBuilder.query(prefixQuery);
            searchSourceBuilder.fetchSource(new String[]{fieldName}, null);
            searchRequest.source(searchSourceBuilder);
            multiRequest.add(searchRequest);
        }
        try {
            MultiSearchResponse mulSearchResponse = client.msearch(multiRequest, RequestOptions.DEFAULT);
            for (MultiSearchResponse.Item item : mulSearchResponse.getResponses()) {
                for (SearchHit hit : item.getResponse().getHits().getHits()) {
                    for (Object value : hit.getSourceAsMap().values()) {
                        if (value != null && StringHelper.isNotEmpty(String.valueOf(value)) && !searchResponseBean.getSetPopUp().contains(value.toString())) {
                            searchResponseBean.getSetPopUp().add(value.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseBean;
    }


    @Override
    public SearchResponseBean searchMerchants(String aliasName, String type, String searchFields, SearchRequestBean sbean) {
        Map<String, Float> fields = getFieldsMap(searchFields);
        SearchResponseBean searchResponseBean = new SearchResponseBean();
        SearchRequest searchMerchantsRequest = new SearchRequest(aliasName);
        searchMerchantsRequest.types(type);
        SearchSourceBuilder searchMerchantsBuilder = new SearchSourceBuilder();
        if (StringHelper.isNotEmpty(sbean.getSort_field())) {
            searchMerchantsBuilder.sort(sbean.getSort_field(),
                    StringHelper.isEmpty(sbean.getSort_order()) || "asc".equals(sbean.getSort_order()) ? SortOrder.ASC : SortOrder.DESC);
        } else {
            searchMerchantsBuilder.sort(SortBuilders.scoreSort().order(SortOrder.DESC));
        }
        if (sbean.getPageNum() != null && sbean.getSize() != null) {
            searchMerchantsBuilder.from((sbean.getPageNum() - 1) * sbean.getSize());
            searchMerchantsBuilder.size(sbean.getSize());
        } else {
            searchMerchantsBuilder.from(0);
            searchMerchantsBuilder.size(10);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("merchants_status", "1"));//搜索正常的商家
        if (StringHelper.isNotEmpty(sbean.getSearchKey())) {
            for (String k : sbean.getSearchKey().split(Constant.BLANKSTR)) {
                if (StringHelper.isNotEmpty(k)) {
                    MultiMatchQueryBuilder multiQuery = QueryBuilders.multiMatchQuery(StringHelper.removeBlank(k)).fields(fields);
                    boolQuery.must(multiQuery);
                }
            }
        }
        searchMerchantsBuilder.query(boolQuery);
        searchMerchantsRequest.source(searchMerchantsBuilder);
        try {
            SearchResponse searchResponse = client.search(searchMerchantsRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            List<String> listMerchantsId = Lists.newArrayList();
            if (CollectionHelp.isArrayNotEmpty(searchHits)) {
                for (SearchHit hit : searchHits) {
                    SearchMerchantsBean b = JSONObject.parseObject(JSONObject.toJSONString(hit.getSourceAsMap()), SearchMerchantsBean.class);
                    b.setScore(hit.getScore());
                    searchResponseBean.getListMerchantsBean().add(b);
                    listMerchantsId.add(b.getMerchants_id());
                }
            }
            CountRequest countRequest = new CountRequest();
            SearchSourceBuilder countSourceBuilder = new SearchSourceBuilder();
            countSourceBuilder.query(boolQuery);
            countRequest.source(countSourceBuilder);
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            searchResponseBean.setTotalNum(countResponse.getCount());
            searchResponseBean.setPageNum(sbean.getPageNum().longValue());
            searchResponseBean.setSize(sbean.getSize().longValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseBean;
    }

    @Override
    public SearchResponseBean searchGoods(String aliasName, String type, String searchFields, SearchRequestBean sbean) {
        Map<String, Float> fields = getFieldsMap(searchFields);
        SearchResponseBean searchResponseBean = new SearchResponseBean();
        SearchRequest searchRequest = new SearchRequest(aliasName);
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringHelper.isNotEmpty(sbean.getSort_field())) {
            searchSourceBuilder.sort(sbean.getSort_field(), StringHelper.isEmpty(sbean.getSort_order()) || "asc".equals(sbean.getSort_order()) ? SortOrder.ASC : SortOrder.DESC);
        } else {
            searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.DESC));
        }
        if (sbean.getPageNum() != null && sbean.getSize() != null) {
            searchSourceBuilder.from((sbean.getPageNum() - 1) * sbean.getSize());
            searchSourceBuilder.size(sbean.getSize());
        } else {
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(50);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termsQuery("goods_status", "1"));

        if (StringHelper.isNotEmpty(sbean.getSearchKey())) {
            for (String k : sbean.getSearchKey().split(Constant.BLANKSTR)) {
                if (StringHelper.isNotEmpty(k)) {
                    MultiMatchQueryBuilder multiQuery = QueryBuilders.multiMatchQuery(StringHelper.removeBlank(k)).fields(fields);
                    boolQuery.must(multiQuery);
                }
            }
        }
        if (CollectionHelp.isArrayNotEmpty(sbean.getGoods_class__name())) {
            boolQuery.must(QueryBuilders.termsQuery("goods_class_name", sbean.getGoods_class__name()));
        }
        if (CollectionHelp.isArrayNotEmpty(sbean.getMerchants_name())) {
            boolQuery.must(QueryBuilders.termsQuery("merchants_name", sbean.getMerchants_name()));
        }
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            if (CollectionHelp.isArrayNotEmpty(searchHits)) {
                for (SearchHit hit : searchHits) {
                    SearchGoodsBean searchGoodsBean = JSONObject.parseObject(JSONObject.toJSONString(hit.getSourceAsMap()), SearchGoodsBean.class);
                    searchGoodsBean.setScore(hit.getScore());
                    searchResponseBean.getListGoodsBean().add(searchGoodsBean);
                }
            }
            CountRequest countRequest = new CountRequest();
            SearchSourceBuilder countSourceBuilder = new SearchSourceBuilder();
            countSourceBuilder.query(boolQuery);
            countRequest.source(countSourceBuilder);
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            searchResponseBean.setTotalNum(countResponse.getCount());
            searchResponseBean.setPageNum(sbean.getPageNum().longValue());
            searchResponseBean.setSize(sbean.getSize().longValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponseBean;
    }


    @Override
    public List<String> getSreens(String aliasName, String type, String searchFields, SearchRequestBean sbean, String buketFieldName, OperatorType operatorType) {
        List<String> l = Lists.newArrayList();
        Map<String, Float> fields = getFieldsMap(searchFields);
        SearchRequest searchRequest = new SearchRequest(aliasName);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringHelper.isNotEmpty(sbean.getSearchKey())) {
            for (String k : sbean.getSearchKey().split(Constant.BLANKSTR)) {
                if (StringHelper.isNotEmpty(k)) {
                    MultiMatchQueryBuilder multiQuery = QueryBuilders.multiMatchQuery(StringHelper.removeBlank(k)).fields(fields);
                    if (OperatorType.AND.equals(operatorType)) {
                        boolQuery.must(multiQuery);
                    } else if (OperatorType.OR.equals(operatorType)) {
                        boolQuery.should(multiQuery);
                    }
                }
            }
            if (OperatorType.OR.equals(operatorType)) {
                boolQuery.minimumShouldMatch(1);
            }
        }
        if ("goods".equals(type)) {
            if (CollectionHelp.isArrayNotEmpty(sbean.getMerchants_name())) {
                boolQuery.must(QueryBuilders.termsQuery("merchants_name", sbean.getMerchants_name()));
            }
            if (CollectionHelp.isArrayNotEmpty(sbean.getGoods_class__name())) {
                boolQuery.must(QueryBuilders.termsQuery("goods_class_name", sbean.getGoods_class__name()));
            }
        }
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.aggregation(AggregationBuilders.terms("aggregationList").field(buketFieldName).size(50));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Object obj = searchResponse.getAggregations().get("aggregationList");
            if (obj instanceof ParsedStringTerms) {
                ParsedStringTerms terms = (ParsedStringTerms) obj;
                for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket : terms.getBuckets()) {
                    String keystring = bucket.getKeyAsString();
                    for (String s : keystring.split(",")) {
                        if (StringHelper.isNotEmpty(s) && !l.contains(s.trim())) {
                            l.add(s.trim());
                        }
                    }
                }
            } else if (obj instanceof ParsedLongTerms) {
                ParsedLongTerms terms = (ParsedLongTerms) obj;
                for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket : terms.getBuckets()) {
                    String keystring = bucket.getKeyAsString();
                    for (String s : keystring.split(",")) {
                        if (StringHelper.isNotEmpty(s) && !l.contains(s.trim())) {
                            l.add(s.trim());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 解析搜索字段
     *
     * @param searchFields
     * @return
     */
    private Map<String, Float> getFieldsMap(String searchFields) {
        String[] fieldsArray = searchFields.split(",");
        Map<String, Float> fields = Maps.newHashMap();
        for (String k : fieldsArray) {
            fields.put(k.split("\\^")[0], Float.parseFloat(k.split("\\^")[1]));
        }
        return fields;
    }
}

