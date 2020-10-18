package com.datuanyuan.annotation;

import java.lang.annotation.*;

/**
 * 定义ES映射字段
 *
 * @author weiyuan
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ESMappingField {
    /**
     * 字段名称
     */
    String fieldName() default "";

    /**
     * 数据类型
     */
    String dataType() default "text";

    /**
     * 指定字段是否需要被分词过滤，主要针对string型字段，如果不过滤则字段精确匹配查询
     *
     * @return
     */
    boolean isAnalyze() default false;

    /**
     * 指定分词词库
     */
    String setAnalyzer() default "";
}
