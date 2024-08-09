package com.fy.qa.ws.imitate.impl;

import com.fy.qa.ws.imitate.InventoryInquiryService;
import org.springframework.stereotype.Component;

@Component
public class InventoryInquiryServiceImpl implements InventoryInquiryService {
    @Override
    public String ZMMWTK01_SSKC_001(String params) {
        String result = "<![CDATA[" +
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<ROOT>\n" +
                "<ITEM>\n" +
                "<ORGNAME>石家庄</ORGNAME>\n" +
                "<KCTYPE>实体库</KCTYPE>\n" +
                "<DMBTR>1111万元</DMBTR>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<ORGNAME>石家庄</ORGNAME>\n" +
                "<KCTYPE>虚拟库</KCTYPE>\n" +
                "<DMBTR>22222万元</DMBTR>\n" +
                "</ITEM>\n" +
                "<ITEM>\n" +
                "<ORGNAME>石家庄</ORGNAME>\n" +
                "<KCTYPE>专业仓</KCTYPE>\n" +
                "<DMBTR>33333万元</DMBTR>\n" +
                "</ITEM>\n" +
                "</ROOT>" +
                "         ]]>";
        return result;
    }
}
