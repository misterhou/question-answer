package com.fy.qa.service.impl;

import com.fy.qa.exception.ParamParserException;
import com.fy.qa.service.IAnswerService;
import com.fy.qa.ws.client.LongShineClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IAnswerServiceImpl implements IAnswerService {

    private LongShineClient longShineClient;

    public IAnswerServiceImpl(LongShineClient longShineClient) {
        this.longShineClient = longShineClient;
    }

    @Override
    public String getAnswer(String question) {
        // 截至到当前时间XX地区（或市县或全省）库存金额是多少
        String answer = null;
        try {
            answer = this.longShineClient.inventoryAmount(question);
        } catch (ParamParserException e) {
            log.error("参数解析错误", e);
            answer = "对不起，" + e.getMessage();
        }
        if (null == answer) {
            answer = "对不起，未查询到对应的答案";
        }
        return answer;
    }
}
