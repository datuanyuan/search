package com.datuanyuan.bean;

import java.util.List;

/**
 * 搜索筛选条件结果
 *
 * @author weiyuan
 * @version 1.0
 */
public class SearchScreenBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7523823817829970666L;

    /**
     * 商家名称筛选条件
     */
    private List<String> merchantsNameBeans;

    /**
     * 商品分类名称筛选条件
     */
    private List<String> goodsClassNameBeans;

    public List<String> getMerchantsNameBeans() {
        return merchantsNameBeans;
    }

    public void setMerchantsNameBeans(List<String> merchantsNameBeans) {
        this.merchantsNameBeans = merchantsNameBeans;
    }

    public List<String> getGoodsClassNameBeans() {
        return goodsClassNameBeans;
    }

    public void setGoodsClassNameBeans(List<String> goodsClassNameBeans) {
        this.goodsClassNameBeans = goodsClassNameBeans;
    }
}