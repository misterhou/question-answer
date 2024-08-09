package com.fy.qa.ws.imitate.impl;

import com.fy.qa.ws.imitate.BillSignService;
import org.springframework.stereotype.Component;

@Component
public class BillSignServiceImpl implements BillSignService {

    @Override
    public String ZMMWTK01_DJTJ_002(String params) {
        String result = "<![CDATA[" +
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<ROOT>\n" +
                "<HEAD>\n" +
                "<ORGNAME>石家庄</ORGNAME>\n" +
                "<SCBILLSUM>3333张</SCBILLSUM>\n" +
                "<QSBILLSUM>2222张</QSBILLSUM>\n" +
                "</HEAD>\n" +
                "<ITEM>\n" +
                "<BILLTYPE>物资收货单</BILLTYPE>\n" +
                "<BILLSUM>111张</BILLSUM>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<BILLTYPE>质保单</BILLTYPE>\n" +
                "<BILLSUM>222张</BILLSUM>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<BILLTYPE>投运单</BILLTYPE>\n" +
                "<BILLSUM>333张</BILLSUM>\n" +
                "</ITEM>\n" +
                "</ROOT>" +
                "         ]]>";
        return result;
    }
}
