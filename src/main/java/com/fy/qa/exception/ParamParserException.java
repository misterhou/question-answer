package com.fy.qa.exception;

import java.util.List;

/**
 * 参数解析异常
 */
public class ParamParserException extends Exception {

    /**
     * 原始问题
     */
    private String question;

    /**
     * 解析到的参数
     */
    private List<String> params;

    public ParamParserException(String question, List<String> params, String message) {
        super(message);
        this.question = question;
        this.params = params;
    }
}
