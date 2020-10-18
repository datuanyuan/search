package com.datuanyuan.service;

import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;

/**
 * 搜索引擎基础元素服务类
 *
 * @author weiyuan
 * @version 1.0
 */
public interface ElasticsearchService {
    /**
     * 创建索引
     *
     * @param indexName
     * @param aliasename
     * @param shards
     * @param replicas
     * @return
     */
    public boolean createIndex(String indexName, String aliasename, Integer shards, Integer replicas);

    /**
     * 添加映射
     *
     * @param indexName
     * @param typeName
     * @param clazz
     * @return
     */
    public boolean addMapping(String indexName, String typeName, Class<?> clazz);

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    public boolean deleteIndex(String indexName);

    /**
     * 重建索引
     *
     * @param old_indexname
     * @param new_indexname
     */
    public void reindex(String old_indexname, String new_indexname);

    /**
     * 重建索引后修改别名
     *
     * @param aliasname
     * @param old_indexname
     * @param new_indexname
     * @return
     */
    public boolean changeAliasAfterReindex(String aliasname, String old_indexname, String new_indexname);

    /**
     * 判断记录是否存在
     *
     * @param aliasName
     * @param typeName
     * @param id
     * @return
     */
    public boolean isExist(String aliasName, String typeName, String id);

    /**
     * 删除数据
     *
     * @param aliasName
     * @param typeName
     * @param id
     * @return
     */
    public boolean deleteData(String aliasName, String typeName, String id);

    /**
     * 根据查询删除数据
     *
     * @param aliasname
     * @param type
     * @param query
     * @return
     */
    public boolean deleteByQuery(String aliasname, String type, QueryBuilder query);

    /**
     * 根据查询更新数据
     *
     * @param aliasname
     * @param type
     * @param query
     * @param script
     * @return
     */
    public boolean updateByQuery(String aliasname, String type, QueryBuilder query, Script script);

    /**
     * 获取各关键字搜锁频率
     *
     * @param returnDataSize
     * @return
     */
    public Map<String, Long> statisticSearchKey(Integer returnDataSize);

    /**
     * 获取单个记录
     *
     * @param aliasname
     * @param type
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getEntity(String aliasname, String type, String id, Class<T> clazz);

    /**
     * 批量删除数据
     *
     * @param aliasName
     * @param typeName
     * @param ids
     * @return
     */
    public boolean deleteBatchData(String aliasName, String typeName, String[] ids);

    /**
     * 保存或者更新数据
     *
     * @param entity
     * @param aliasName
     * @param typeName
     * @param id
     * @param <T>
     * @return
     */
    public <T> Boolean saveOrUpdateEntity(T entity, String aliasName, String typeName, String id);
}
