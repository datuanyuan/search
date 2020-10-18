package com.datuanyuan.bean;

/**
 * 
 * <pre>
 * 操作符类型
 * </pre>
 * 
 * @author weiyuan
 */
public enum OperatorType {
    /**
     * 与操作符
     */
    AND("与操作符"),
    /**
     * 或操作符
     */
    OR("或操作符");
    private String text;

    OperatorType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
