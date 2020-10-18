package com.datuanyuan.bean;


import com.datuanyuan.annotation.ESMappingField;
import com.datuanyuan.annotation.ESMappingType;

/**
 * 关键字搜索频率
 *
 * @author weiyuan
 * @version 1.0
 */
@ESMappingType(typeName = "keysval")
public class SearchKeyBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8730115232519582382L;

    public SearchKeyBean(String search_key, String search_type) {
        this.searh_key = search_key;
        this.search_type = search_type;
    }

    public SearchKeyBean(String search_key, Long search_times) {
        this.searh_key = search_key;
        this.search_times = search_times;
    }

    /**
     * 关键字
     */
    @ESMappingField(fieldName = "searh_key", dataType = "keyword")
    private String searh_key;
    /**
     * 关键字类型
     */
    @ESMappingField(fieldName = "search_type", dataType = "keyword")
    private String search_type;
    /**
     * 搜索次数
     */
    private Long search_times;

    public String getSearh_key() {
        return searh_key;
    }

    public void setSearh_key(String searh_key) {
        this.searh_key = searh_key;
    }

    public String getSearch_type() {
        return search_type;
    }

    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }

    public Long getSearch_times() {
        return search_times;
    }

    public void setSearch_times(Long search_times) {
        this.search_times = search_times;
    }
}
