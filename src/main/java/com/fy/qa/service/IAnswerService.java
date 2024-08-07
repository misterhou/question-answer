package com.fy.qa.service;

public interface IAnswerService {

    /**
     * 根据问题获取答案
     * @param question 问题
     * @return 问题对应的答案
     */
    String getAnswer(String question);
}
