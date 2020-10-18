package com.datuanyuan.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.datuanyuan.annotation.ESMappingField;
import com.datuanyuan.annotation.ESMappingType;
import com.datuanyuan.bean.SymbolType;
import com.datuanyuan.service.ElasticsearchService;
import com.datuanyuan.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private Log logger = LogFactory.getLog(ElasticsearchServiceImpl.class);

    @Autowired
    public RestHighLevelClient client;

    @Value("${search.simple.pattern.split.symbol}")
    public String simplePatternSplitSymbol;

    @Value("${search.searchkey.alias}")
    private String searchKeyAlias;

    @Value("${search.searchkey.type}")
    private String searchKeyType;

    @Override
    public boolean createIndex(String indexName, String aliasename, Integer shards, Integer replicas) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        Settings.Builder builder = Settings.builder().put("index.mapper.dynamic", false)
                .put("index.number_of_shards", shards == null ? 1 : shards)
                .put("index.number_of_replicas", replicas == null ? 1 : replicas)
                .put("index.max_result_window", 1000000);
        if (StringHelper.isNotEmpty(simplePatternSplitSymbol)) {
            for (String s : simplePatternSplitSymbol.split(",")) {
                SymbolType symbolType = SymbolType.valueOf(s);
                String analyzerName = "customer_split_" + symbolType.name() + "_analyzer";
                String tokenizerName = "customer_split_" + symbolType.name() + "_tokenizer";
                builder = builder.put("index.analysis.analyzer." + analyzerName + ".tokenizer", tokenizerName)
                        .put("index.analysis.tokenizer." + tokenizerName + ".type", "simple_pattern_split")
                        .put("index.analysis.tokenizer." + tokenizerName + ".pattern", symbolType.getText());
            }
        }
        request.settings(builder);
        if (StringHelper.isNotEmpty(aliasename)) {
            request.alias(new Alias(aliasename));
        }
        request.timeout(TimeValue.timeValueMinutes(1));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(2));
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
            if (acknowledged && shardsAcknowledged) {
                logger.info("索引创建成功");
            }
            return acknowledged && shardsAcknowledged;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addMapping(String indexName, String typeName, Class<?> clazz) {
        if (clazz.getAnnotation(ESMappingType.class) == null) {
            return true;
        }
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.type(typeName);

        Map<String, Object> jsonMap = Maps.newHashMap();
        Map<String, Object> properties = Maps.newHashMap();

        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            ESMappingField esMappingField = f.getAnnotation(ESMappingField.class);
            if (esMappingField != null) {
                String fieldname = esMappingField.fieldName();
                String datatype = esMappingField.dataType();
                String analyzer = esMappingField.setAnalyzer();
                boolean isanalye = esMappingField.isAnalyze();

                Map<String, Object> m = Maps.newHashMap();
                m.put("type", datatype);
                if (isanalye && StringHelper.isNotEmpty(analyzer)) {
                    m.put("analyzer", analyzer);
                    m.put("search_analyzer", analyzer);
                }
                properties.put(fieldname, m);
            }
        }
        jsonMap.put("properties", properties);
        putMappingRequest.source(jsonMap);
        putMappingRequest.timeout(TimeValue.timeValueMinutes(2));
        try {
            AcknowledgedResponse putMappingResponse = client.indices().putMapping(putMappingRequest,
                    RequestOptions.DEFAULT);
            if (putMappingResponse.isAcknowledged()) {
            }
            return putMappingResponse.isAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteIndex(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(3));
        try {
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
            boolean acknowledged = deleteIndexResponse.isAcknowledged();
            if (acknowledged) {
            }
            return acknowledged;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
            }
        }
        return false;
    }

    @Override
    public void reindex(String old_indexname, String new_indexname) {
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices(old_indexname);
        request.setDestIndex(new_indexname);
        request.setDestVersionType(VersionType.EXTERNAL);
        request.setSourceBatchSize(1000);
        request.setDestOpType("create");
        request.setConflicts("proceed");
        request.setScroll(TimeValue.timeValueMinutes(10));
        request.setTimeout(TimeValue.timeValueMinutes(20));
        request.setRefresh(true);
        try {
            BulkByScrollResponse bulkResponse = client.reindex(request, RequestOptions.DEFAULT);
            boolean timedOut = bulkResponse.isTimedOut();
            long totalDocs = bulkResponse.getTotal();
            long updatedDocs = bulkResponse.getUpdated();
            long createdDocs = bulkResponse.getCreated();
            long deletedDocs = bulkResponse.getDeleted();
            long batches = bulkResponse.getBatches();
            long noops = bulkResponse.getNoops();
            long versionConflicts = bulkResponse.getVersionConflicts();
            long bulkRetries = bulkResponse.getBulkRetries();
            long searchRetries = bulkResponse.getSearchRetries();
            TimeValue throttledMillis = bulkResponse.getStatus().getThrottled();
            TimeValue throttledUntilMillis = bulkResponse.getStatus().getThrottledUntil();
            List<ScrollableHitSource.SearchFailure> searchFailures = bulkResponse.getSearchFailures();
            List<BulkItemResponse.Failure> bulkFailures = bulkResponse.getBulkFailures();

            logger.info("timedOut: " + timedOut);
            logger.info("totalDocs: " + totalDocs);
            logger.info("updatedDocs: " + updatedDocs);
            logger.info("createdDocs: " + createdDocs);
            logger.info("deletedDocs: " + deletedDocs);
            logger.info("batches: " + batches);
            logger.info("noops: " + noops);
            logger.info("versionConflicts: " + versionConflicts);
            logger.info("bulkRetries: " + bulkRetries);
            logger.info("searchRetries: " + searchRetries);
            logger.info("throttledMillis: " + throttledMillis.toString());
            logger.info("throttledUntilMillis: " + throttledUntilMillis.toString());
            logger.info("searchFailures: " + searchFailures.size());
            logger.info("bulkFailures: " + bulkFailures.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean changeAliasAfterReindex(String aliasname, String old_indexname, String new_indexname) {
        IndicesAliasesRequest.AliasActions addIndexAction = new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.ADD).index(new_indexname).alias(aliasname);
        IndicesAliasesRequest.AliasActions removeAction = new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.REMOVE).index(old_indexname).alias(aliasname);

        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        indicesAliasesRequest.addAliasAction(addIndexAction);
        indicesAliasesRequest.addAliasAction(removeAction);
        try {
            AcknowledgedResponse indicesAliasesResponse = client.indices().updateAliases(indicesAliasesRequest,
                    RequestOptions.DEFAULT);
            boolean acknowledged = indicesAliasesResponse.isAcknowledged();
            if (acknowledged) {
            }
            return acknowledged;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断新增的记录是否已存在
     *
     * @return
     * @throws IOException
     */
    @Override
    public boolean isExist(String aliasName, String typeName, String id) {
        GetRequest getRequest = new GetRequest(aliasName, typeName, id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        try {
            return client.exists(getRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteData(String aliasName, String typeName, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(aliasName, typeName, id);
        deleteRequest.timeout(TimeValue.timeValueMinutes(20));
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        deleteRequest.setRefreshPolicy("wait_for");
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public <T> Boolean saveOrUpdateEntity(T entity, String aliasName, String typeName, String id) {
        try {
            if (StringHelper.isNotEmpty(id)) {
                if (this.isExist(aliasName, typeName, id)) {
                    UpdateRequest updateRequest = new UpdateRequest(aliasName, typeName, id);
                    updateRequest.doc(JSONObject.toJSONString(entity), XContentType.JSON);
                    UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
                    if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                        logger.info("CREATED！");
                    } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                        logger.info("UPDATED！");
                    } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
                        logger.info("DELETED！");
                    } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
                        logger.info("NOOP！");
                    }
                    return true;
                } else {
                    String jsonString = JSONObject.toJSONString(entity);
                    IndexRequest indexRequest = new IndexRequest(aliasName, typeName, id);
                    indexRequest.source(jsonString, XContentType.JSON);
                    IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                    if (indexResponse != null && "CREATED".equals(indexResponse.getResult().toString())) {
                        logger.info("CREATED！");
                        return true;
                    }
                }
            } else {
                String jsonString = JSONObject.toJSONString(entity);
                IndexRequest indexRequest = new IndexRequest(aliasName, typeName);
                indexRequest.source(jsonString, XContentType.JSON);
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                if (indexResponse != null && "CREATED".equals(indexResponse.getResult().toString())) {
                    logger.info("CREATED！");
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean deleteByQuery(String aliasname, String type, QueryBuilder query) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(aliasname);
        request.types(type);
        request.setConflicts("proceed");
        request.setQuery(query);
        request.setTimeout(TimeValue.timeValueMinutes(10));
        request.setRefresh(true);
        request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        try {
            BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);
            boolean timedOut = bulkResponse.isTimedOut();
            long totalDocs = bulkResponse.getTotal();
            long deletedDocs = bulkResponse.getDeleted();
            long batches = bulkResponse.getBatches();
            long noops = bulkResponse.getNoops();
            long versionConflicts = bulkResponse.getVersionConflicts();
            long bulkRetries = bulkResponse.getBulkRetries();
            long searchRetries = bulkResponse.getSearchRetries();
            logger.info("timedOut: " + timedOut);
            logger.info("totalDocs: " + totalDocs);
            logger.info("deletedDocs: " + deletedDocs);
            logger.info("batches: " + batches);
            logger.info("noops: " + noops);
            logger.info("versionConflicts: " + versionConflicts);
            logger.info("bulkRetries: " + bulkRetries);
            logger.info("searchRetries: " + searchRetries);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateByQuery(String aliasname, String type, QueryBuilder query, Script script) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(aliasname);
        request.setDocTypes(type);
        request.setConflicts("proceed");
        request.setQuery(query);
        request.setScript(script);
        request.setTimeout(TimeValue.timeValueMinutes(10));
        request.setRefresh(true);
        try {
            BulkByScrollResponse bulkResponse = client.updateByQuery(request, RequestOptions.DEFAULT);
            boolean timedOut = bulkResponse.isTimedOut();
            long totalDocs = bulkResponse.getTotal();
            long deletedDocs = bulkResponse.getDeleted();
            long batches = bulkResponse.getBatches();
            long noops = bulkResponse.getNoops();
            long versionConflicts = bulkResponse.getVersionConflicts();
            long bulkRetries = bulkResponse.getBulkRetries();
            long searchRetries = bulkResponse.getSearchRetries();
            logger.info("timedOut: " + timedOut);
            logger.info("totalDocs: " + totalDocs);
            logger.info("deletedDocs: " + deletedDocs);
            logger.info("batches: " + batches);
            logger.info("noops: " + noops);
            logger.info("versionConflicts: " + versionConflicts);
            logger.info("bulkRetries: " + bulkRetries);
            logger.info("searchRetries: " + searchRetries);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Long> statisticSearchKey(Integer returnDataSize) {
        Map<String, Long> map = Maps.newHashMap();
        SearchRequest searchRequest = new SearchRequest(searchKeyAlias);
        searchRequest.types(searchKeyType);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms("aggregationList").field("searh_key")
                .size(returnDataSize));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            ParsedStringTerms terms = searchResponse.getAggregations().get("aggregationList");
            for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket : terms.getBuckets()) {
                map.put(bucket.getKeyAsString(), bucket.getDocCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public <T> T getEntity(String aliasname, String type, String id, Class<T> clazz) {
        GetRequest getRequest = new GetRequest(aliasname, type, id);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                return JSONObject.parseObject(JSONObject.toJSONString(getResponse.getSourceAsMap()), clazz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteBatchData(String aliasName, String typeName, String[] ids) {
        BulkRequest bulkRequest = new BulkRequest();
        for (String id : ids) {
            DeleteRequest del = new DeleteRequest(aliasName, typeName, id);
            bulkRequest.add(del);
        }
        bulkRequest.timeout(TimeValue.timeValueMinutes(40));
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
