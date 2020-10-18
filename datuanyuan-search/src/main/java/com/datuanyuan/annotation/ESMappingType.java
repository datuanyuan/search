package com.datuanyuan.annotation;


import java.lang.annotation.*;

/**
 * 定义ES映射类
 *
 * @author weiyuan
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ESMappingType {
    /**
     * 映射的文档类型
     *
     * @return
     */
    String typeName() default "";
}
