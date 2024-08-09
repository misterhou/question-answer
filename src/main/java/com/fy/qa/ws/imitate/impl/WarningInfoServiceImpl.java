package com.fy.qa.ws.imitate.impl;

import com.fy.qa.ws.imitate.WarningInfoService;
import org.springframework.stereotype.Component;


@Component
public class WarningInfoServiceImpl implements WarningInfoService {
    @Override
    public String ZMMWTK01_YJTJ_003(String params) {
        String result = "<![CDATA[" +
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<ROOT>\n" +
                "<HEAD>\n" +
                "<ORGNAME>石家庄</ORGNAME>\n" +
                "<WARNHZSUM>3333条</WARNHZSUM>\n" +
                "</HEAD>\n" +
                "<ITEM>\n" +
                "<WARNLEVEL>红色告警</WARNLEVEL>\n" +
                "<WARNSUM>111条</WARNSUM>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<WARNLEVEL>黄色预警</WARNLEVEL>\n" +
                "<WARNSUM>222条</WARNSUM>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<WARNLEVEL>蓝色提醒</WARNLEVEL>\n" +
                "<WARNSUM>333条</WARNSUM>\n" +
                "</ITEM>\n" +
                "</ROOT>" +
                "         ]]>";
        return result;
    }
}
