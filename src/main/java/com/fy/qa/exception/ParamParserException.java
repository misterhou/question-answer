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

    public ParamParserException(String message, String question) {
        super(message);
        this.question = question;
    }
}
