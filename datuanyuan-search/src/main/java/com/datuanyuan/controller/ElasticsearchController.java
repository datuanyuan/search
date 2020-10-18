package com.datuanyuan.controller;

import java.io.IOException;

import com.datuanyuan.service.ElasticsearchService;
import com.datuanyuan.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 搜索引擎基础数据维护
 *
 * @author weiyuan
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/elasticsearch/v1.0/")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * 创建索引
     *
     * @param indexName
     * @param aliasName
     * @param shards
     * @param replicas
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/creatindex", method = RequestMethod.POST)
    public Object creatindex(String indexName, String aliasName, Integer shards, Integer replicas) throws IOException {
        try {
            if (elasticsearchService.createIndex(indexName, aliasName, shards, replicas)) {
                return "creatindex success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "creatindex fail";
    }

    /**
     * 添加数据映射
     *
     * @param indexName
     * @param typeName
     * @param classname
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/addmapping", method = RequestMethod.POST)
    public Object addmapping(String indexName, String typeName, String classname) throws IOException {
        try {
            Class<?> clazz = Class.forName(classname);
            if (elasticsearchService.addMapping(indexName, typeName, clazz)) {
                return "createmapping success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "createmapping fail";
    }

    /**
     * 重建索引
     *
     * @param old_indexname
     * @param new_indexname
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/reindex", method = RequestMethod.POST)
    public Object reindex(String old_indexname, String new_indexname) throws IOException {
        elasticsearchService.reindex(old_indexname, new_indexname);
        return "success";
    }

    /**
     * 重建索引后修改别名
     *
     * @param aliasname
     * @param old_indexname
     * @param new_indexname
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/changeAliasAfterReindex", method = RequestMethod.POST)
    public Object changeAliasAfterReindex(String aliasname, String old_indexname, String new_indexname)
            throws IOException {
        if (elasticsearchService.changeAliasAfterReindex(aliasname, old_indexname, new_indexname)) {
            return "changeAlias success";
        }
        return "changeAlias fail";
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/deleteindex", method = RequestMethod.POST)
    public Object deleteindex(String indexName) throws IOException {
        if (elasticsearchService.deleteIndex(indexName)) {
            return "deleteindex success";
        }
        return "deleteindex fail";
    }

    /**
     * 删除数据
     *
     * @param aliasename
     * @param type
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletedata", method = RequestMethod.POST)
    public Object deletedata(String aliasename, String type, String id) {
        if (elasticsearchService.deleteData(aliasename, type, id)) {
            return "deletedata success";
        }
        return "deletedata fail";
    }

    /**
     * 批量删除数据
     *
     * @param aliasename
     * @param type
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatchdata", method = RequestMethod.POST)
    public Object deleteBatchdata(String aliasename, String type, String ids) {
        if (StringHelper.isEmpty(ids)) {
            return "no data";
        }
        if (elasticsearchService.deleteBatchData(aliasename, type, ids.split(","))) {
            return "deleteBatchdata success";
        }
        return "deleteBatchdata fail";
    }
}