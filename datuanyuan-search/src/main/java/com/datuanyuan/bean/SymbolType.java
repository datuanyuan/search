package com.datuanyuan.bean;

/**
 * 特殊符号枚举类型
 * 
 * @author weiyuan
 *
 */
public enum SymbolType {
    /**
     * 逗号
     */
    comma(","),
    /**
     * 下划线
     */
    underline("_"),
    /**
     * 与字符
     */
    and("&"),
    /**
     * 垂直线
     */
    verline("|");

    private String text;

    SymbolType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
