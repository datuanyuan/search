package com.datuanyuan.bean;

import com.datuanyuan.annotation.ESMappingField;
import com.datuanyuan.annotation.ESMappingType;

/**
 * 商品实体类
 *
 * @author weiyuan
 * @version 1.0
 */
@ESMappingType(typeName = "goods")
public class SearchGoodsBean implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3407466587287002039L;
    /**
     * 商家ID
     */
    @ESMappingField(fieldName = "merchants_id", dataType = "keyword")
    private String merchants_id;
    /**
     * 商家名称
     */
    @ESMappingField(fieldName = "merchants_name", dataType = "keyword")
    private String merchants_name;

    /**
     * 商家名称，使用ik_max_word进行分词
     */
    @ESMappingField(fieldName = "merchants_name_ik", dataType = "text", isAnalyze = true, setAnalyzer = "ik_max_word")
    private String merchants_name_ik;
    /**
     * 商品ID
     */
    @ESMappingField(fieldName = "goods_id", dataType = "keyword")
    private String goods_id;
    /**
     * 商品名称-不分词
     */
    @ESMappingField(fieldName = "goods_name", dataType = "text", isAnalyze = true, setAnalyzer = "keyword")
    private String goods_name;
    /**
     * 商品名称-使用ik_max_word分词
     */
    @ESMappingField(fieldName = "goods_name_ik", dataType = "text", isAnalyze = true, setAnalyzer = "ik_max_word")
    private String goods_name_ik;
    /**
     * 商品名称-使用smartcn分词
     */
    @ESMappingField(fieldName = "goods_name_smart", dataType = "text", isAnalyze = true, setAnalyzer = "smartcn")
    private String goods_name_smart;
    /**
     * 商品名称-使用标准分词器
     */
    @ESMappingField(fieldName = "goods_name_standard", dataType = "text", isAnalyze = true, setAnalyzer = "standard")
    private String goods_name_standard;
    /**
     * 商品分类ID
     */
    @ESMappingField(fieldName = "goods_class_id", dataType = "keyword")
    private String goods_class_id;
    /**
     * 商品分类名称
     */
    @ESMappingField(fieldName = "goods_class_name", dataType = "keyword")
    private String goods_class_name;
    /**
     * 商品图片
     */
    @ESMappingField(fieldName = "goods_img", dataType = "keyword")
    private String goods_img;
    /**
     * 商品价格
     */
    @ESMappingField(fieldName = "goods_price", dataType = "long")
    private Long goods_price;
    /**
     * 商品状态，1正常，0下架，-1已删除
     */
    @ESMappingField(fieldName = "goods_status", dataType = "keyword")
    private String goods_status;
    /**
     * 商品销量
     */
    @ESMappingField(fieldName = "goods_sales", dataType = "long")
    private Long goods_sales;
    /**
     * 商品库存
     */
    @ESMappingField(fieldName = "goods_num", dataType = "integer")
    private Integer goods_num;
    /**
     * 搜索结果匹配度
     */
    private float score;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getGoods_name_ik() {
        return goods_name_ik;
    }

    public void setGoods_name_ik(String goods_name_ik) {
        this.goods_name_ik = goods_name_ik;
    }

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

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_name_smart() {
        return goods_name_smart;
    }

    public void setGoods_name_smart(String goods_name_smart) {
        this.goods_name_smart = goods_name_smart;
    }

    public String getGoods_name_standard() {
        return goods_name_standard;
    }

    public void setGoods_name_standard(String goods_name_standard) {
        this.goods_name_standard = goods_name_standard;
    }

    public String getGoods_class_id() {
        return goods_class_id;
    }

    public void setGoods_class_id(String goods_class_id) {
        this.goods_class_id = goods_class_id;
    }

    public String getGoods_class_name() {
        return goods_class_name;
    }

    public void setGoods_class_name(String goods_class_name) {
        this.goods_class_name = goods_class_name;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public Long getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(Long goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public Long getGoods_sales() {
        return goods_sales;
    }

    public void setGoods_sales(Long goods_sales) {
        this.goods_sales = goods_sales;
    }

    public Integer getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(Integer goods_num) {
        this.goods_num = goods_num;
    }
}