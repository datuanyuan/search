package com.datuanyuan.bean;

import com.datuanyuan.annotation.ESMappingField;
import com.datuanyuan.annotation.ESMappingType;

import java.util.List;

/**
 * 商家信息
 *
 * @author weiyuan
 * @version 1.0
 */
@ESMappingType(typeName = "merchants")
public class SearchMerchantsBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6762419767390093593L;

    /**
     * 商家ID
     */
    @ESMappingField(fieldName = "merchants_id", dataType = "keyword")
    private String merchants_id;
    /**
     * 商家名称-不分词
     */
    @ESMappingField(fieldName = "merchants_name", dataType = "keyword")
    private String merchants_name;
    /**
     * 商家名称-使用IK分词
     */
    @ESMappingField(fieldName = "merchants_name_ik", dataType = "text", isAnalyze = true, setAnalyzer = "ik_max_word")
    private String merchants_name_ik;
    /**
     * 商家名称-使用smart分词
     */
    @ESMappingField(fieldName = "merchants_name_smart", dataType = "text", isAnalyze = true, setAnalyzer = "smartcn")
    private String merchants_name_smart;
    /**
     * 商家名称-使用标准分词器
     */
    @ESMappingField(fieldName = "merchants_name_standard", dataType = "text", isAnalyze = true, setAnalyzer = "standard")
    private String merchants_name_standard;
    /**
     * 商家logo
     */
    @ESMappingField(fieldName = "merchants_logo", dataType = "keyword")
    private String merchants_logo;
    /**
     * 商家总销量
     */
    @ESMappingField(fieldName = "merchants_total_sales_num", dataType = "long")
    private Long merchants_total_sales_num;
    /**
     * 商家在售商品数量
     */
    @ESMappingField(fieldName = "merchants_on_saling_goods_num", dataType = "long")
    private Long merchants_on_saling_goods_num;
    /**
     * 商家特色
     */
    @ESMappingField(fieldName = "merchants_service_feature", dataType = "text", isAnalyze = true, setAnalyzer = "ik_max_word")
    private String merchants_service_feature;
    /**
     * 商家所在地区
     */
    @ESMappingField(fieldName = "merchants_province", dataType = "keyword")
    private String merchants_province;
    /**
     * 商家状态，1正常，0关闭，-1删除
     */
    @ESMappingField(fieldName = "merchants_status", dataType = "keyword")
    private String merchants_status;
    /**
     * 搜索结果匹配度
     */
    private float score;

    private List<SearchGoodsBean> listGoodsBean;

    public String getMerchants_id() {
        return merchants_id;
    }

    public void setMerchants_id(String merchants_id) {
        this.merchants_id = merchants_id;
    }

    public String getMerchants_name() {
        return merchants_name;
    }

    public void setMerchants_name(String merchants_name) {
        this.merchants_name = merchants_name;
    }

    public String getMerchants_name_ik() {
        return merchants_name_ik;
    }

    public void setMerchants_name_ik(String merchants_name_ik) {
        this.merchants_name_ik = merchants_name_ik;
    }

    public String getMerchants_name_smart() {
        return merchants_name_smart;
    }

    public void setMerchants_name_smart(String merchants_name_smart) {
        this.merchants_name_smart = merchants_name_smart;
    }

    public String getMerchants_name_standard() {
        return merchants_name_standard;
    }

    public void setMerchants_name_standard(String merchants_name_standard) {
        this.merchants_name_standard = merchants_name_standard;
    }

    public String getMerchants_logo() {
        return merchants_logo;
    }

    public void setMerchants_logo(String merchants_logo) {
        this.merchants_logo = merchants_logo;
    }

    public Long getMerchants_total_sales_num() {
        return merchants_total_sales_num;
    }

    public void setMerchants_total_sales_num(Long merchants_total_sales_num) {
        this.merchants_total_sales_num = merchants_total_sales_num;
    }

    public Long getMerchants_on_saling_goods_num() {
        return merchants_on_saling_goods_num;
    }

    public void setMerchants_on_saling_goods_num(Long merchants_on_saling_goods_num) {
        this.merchants_on_saling_goods_num = merchants_on_saling_goods_num;
    }

    public String getMerchants_service_feature() {
        return merchants_service_feature;
    }

    public void setMerchants_service_feature(String merchants_service_feature) {
        this.merchants_service_feature = merchants_service_feature;
    }

    public String getMerchants_province() {
        return merchants_province;
    }

    public void setMerchants_province(String merchants_province) {
        this.merchants_province = merchants_province;
    }

    public String getMerchants_status() {
        return merchants_status;
    }

    public void setMerchants_status(String merchants_status) {
        this.merchants_status = merchants_status;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<SearchGoodsBean> getListGoodsBean() {
        return listGoodsBean;
    }

    public void setListGoodsBean(List<SearchGoodsBean> listGoodsBean) {
        this.listGoodsBean = listGoodsBean;
    }
}
