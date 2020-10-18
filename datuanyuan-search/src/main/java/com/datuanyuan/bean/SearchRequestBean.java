package com.datuanyuan.bean;

/**
 * 搜索请求实体类
 *
 * @author weiyuan
 * @version 1.0
 */
public class SearchRequestBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2456950703191701366L;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer size;
    /**
     * 搜索关键字，多个空格隔开
     */
    private String searchKey;
    /**
     * 排序字段名称
     */
    private String sort_field;
    /**
     * 排序规则，asc升序，desc降序
     */
    private String sort_order;

    /**
     * 商家筛选条件
     */
    private String[] merchants_name;
    /**
     * 商品分类筛选条件
     */
    private String[] goods_class__name;

    public String[] getMerchants_name() {
        return merchants_name;
    }

    public void setMerchants_name(String[] merchants_name) {
        this.merchants_name = merchants_name;
    }

    public String[] getGoods_class__name() {
        return goods_class__name;
    }

    public void setGoods_class__name(String[] goods_class__name) {
        this.goods_class__name = goods_class__name;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSort_field() {
        return sort_field;
    }

    public void setSort_field(String sort_field) {
        this.sort_field = sort_field;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }
}
