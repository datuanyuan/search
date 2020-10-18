package com.datuanyuan.bean;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 搜索响应消息实体类
 *
 * @author weiyuan
 * @version 1.0
 */
public class SearchResponseBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3407466587287002039L;
    /**
     * 商家集合
     */
    private List<SearchMerchantsBean> listMerchantsBean = Lists.newArrayList();
    /**
     * 商品集合
     */
    private List<SearchGoodsBean> listGoodsBean = Lists.newArrayList();
    /**
     * 关键字联想集合
     */
    private Set<String> setPopUp = Sets.newHashSet();
    /**
     * 分页页码
     */
    private Long pageNum;
    /**
     * 分页-每页数量
     */
    private Long size;
    /**
     * 总记录条数
     */
    private Long totalNum;
    /**
     * 筛选条件实体
     */
    private SearchScreenBean searchScreenBean;

    public List<SearchMerchantsBean> getListMerchantsBean() {
        return listMerchantsBean;
    }

    public void setListMerchantsBean(List<SearchMerchantsBean> listMerchantsBean) {
        this.listMerchantsBean = listMerchantsBean;
    }

    public List<SearchGoodsBean> getListGoodsBean() {
        return listGoodsBean;
    }

    public void setListGoodsBean(List<SearchGoodsBean> listGoodsBean) {
        this.listGoodsBean = listGoodsBean;
    }

    public Set<String> getSetPopUp() {
        return setPopUp;
    }

    public void setSetPopUp(Set<String> setPopUp) {
        this.setPopUp = setPopUp;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public SearchScreenBean getSearchScreenBean() {
        return searchScreenBean;
    }

    public void setSearchScreenBean(SearchScreenBean searchScreenBean) {
        this.searchScreenBean = searchScreenBean;
    }
}
