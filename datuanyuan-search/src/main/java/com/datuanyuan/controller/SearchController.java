package com.datuanyuan.controller;

import java.util.List;
import java.util.Map;

import com.datuanyuan.bean.*;
import com.datuanyuan.service.ElasticsearchService;
import com.datuanyuan.service.SearchService;
import com.datuanyuan.util.CollectionHelp;
import com.datuanyuan.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 搜索引擎搜索请求入口
 *
 * @author weiyuan
 * @version 1.0
 */
@Controller
@RequestMapping("/search/v1.0")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Value("${search.searchkey.alias}")
    private String searchKeyAlias;

    @Value("${search.searchkey.type}")
    private String searchKeyType;

    @Value("${search.goods.alias}")
    private String searchGoodsAlias;

    @Value("${search.goods.type}")
    private String searchGoodsType;

    @Value("${search.goods.fields}")
    private String searchGoodsField;

    @Value("${search.merchants.alias}")
    private String searchMerchantsAlias;

    @Value("${search.merchants.type}")
    private String searchMerchantsType;

    @Value("${search.merchants.fields}")
    private String searchMerchantsField;

    /**
     * 搜索商品入口
     * @param searchRequestBean
     * @return
     */
    @RequestMapping(value = "/searchGoods", method = RequestMethod.POST)
    public Object searchGoods(SearchRequestBean searchRequestBean) {
        if (StringHelper.isNotEmpty(searchRequestBean.getSearchKey())) {//保存用户搜索的关键字
            elasticsearchService.saveOrUpdateEntity(new SearchKeyBean(searchRequestBean.getSearchKey(), "goods"),
                    searchKeyAlias, searchKeyType, "");
        }
        //搜索商品
        SearchResponseBean response = searchService.searchGoods(searchGoodsAlias, searchGoodsType, searchGoodsField, searchRequestBean);

        SearchScreenBean searchScreenBean = new SearchScreenBean();

        searchScreenBean.setGoodsClassNameBeans(searchService.getSreens(searchGoodsAlias, searchGoodsType, searchGoodsField, searchRequestBean,
                "goods_class_name", OperatorType.AND));

        searchScreenBean.setMerchantsNameBeans(searchService.getSreens(searchGoodsAlias, searchGoodsType, searchGoodsField, searchRequestBean,
                "merchants_name", OperatorType.AND));

        response.setSearchScreenBean(searchScreenBean);
        return response;
    }

    /**
     * 搜索商家
     *
     * @param searchRequestBean
     * @return
     */
    @RequestMapping(value = "/searchMerchants", method = RequestMethod.POST)
    public Object searchMerchants(SearchRequestBean searchRequestBean) {
        if (StringHelper.isNotEmpty(searchRequestBean.getSearchKey())) {//保存搜索关键字
            elasticsearchService.saveOrUpdateEntity(new SearchKeyBean(searchRequestBean.getSearchKey(), "merchants"),
                    searchMerchantsAlias, searchMerchantsType, "");
        }
        //搜索商家
        SearchResponseBean searchResponseBean = searchService.searchMerchants(searchMerchantsAlias, searchMerchantsType, searchMerchantsField, searchRequestBean);

        /**
         * 聚合商家名称搜索条件
         */
        SearchScreenBean searchScreenBean = new SearchScreenBean();
        searchScreenBean.setMerchantsNameBeans(searchService.getSreens(searchMerchantsAlias, searchMerchantsType, searchMerchantsField,
                searchRequestBean, "merchants_name", OperatorType.AND));

        searchResponseBean.setSearchScreenBean(searchScreenBean);
        return searchResponseBean;
    }


    /**
     * 关键词联想功能
     *
     * @param searchRequestBean
     * @return
     */
    @RequestMapping(value = "/searchKeyWordPopup", method = RequestMethod.POST)
    public Object searchKeyWordPopup(SearchRequestBean searchRequestBean) {
        SearchResponseBean searchResponseBean = searchService.searchKeyWordPopup(searchGoodsAlias, searchGoodsType, searchGoodsField, searchRequestBean);
        return searchResponseBean;
    }

    /**
     * 获取关键字搜索排名
     *
     * @param returnDataSize
     * @return
     */
    @RequestMapping(value = "/statisticSearchKey", method = RequestMethod.POST)
    public Object statisticSearchKey(Integer returnDataSize) {
        Map<String, Long> map = CollectionHelp.mapSortByValueDesc(elasticsearchService
                .statisticSearchKey(returnDataSize != null && returnDataSize > 0 ? returnDataSize : 50));
        List<SearchKeyBean> list = Lists.newArrayList();
        for (String key : map.keySet()) {
            list.add(new SearchKeyBean(key, map.get(key)));
        }
        return list;
    }
}
