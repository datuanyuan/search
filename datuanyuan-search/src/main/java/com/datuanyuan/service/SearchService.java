package com.datuanyuan.service;

import com.datuanyuan.bean.OperatorType;
import com.datuanyuan.bean.SearchRequestBean;
import com.datuanyuan.bean.SearchResponseBean;

import java.util.List;
import java.util.Map;

/**
 * 搜索引擎服务类
 *
 * @author weiyuan
 * @version 1.0
 */
public interface SearchService {
    /**
     * 搜索商品
     *
     * @param aliasName
     * @param type
     * @param searchFields
     * @param searchRequestBean
     * @return
     */
    public SearchResponseBean searchGoods(String aliasName, String type, String searchFields, SearchRequestBean searchRequestBean);

    /**
     * 搜索商家
     *
     * @param aliasName
     * @param type
     * @param searchFields
     * @param searchRequestBean
     * @return
     */
    public SearchResponseBean searchMerchants(String aliasName, String type, String searchFields, SearchRequestBean searchRequestBean);

    /**
     * 关键字联想功能
     *
     * @param aliasName
     * @param type
     * @param searchFields
     * @param searchRequestBean
     * @return
     */
    public SearchResponseBean searchKeyWordPopup(String aliasName, String type, String searchFields, SearchRequestBean searchRequestBean);

    /**
     * 获取结果集中筛选条件
     *
     * @param aliasName
     * @param type
     * @param searchFields
     * @param searchRequestBean
     * @param buketFieldName
     * @param operatorType
     * @return
     */
    public List<String> getSreens(String aliasName, String type, String searchFields, SearchRequestBean searchRequestBean, String buketFieldName,
                                  OperatorType operatorType);


}
