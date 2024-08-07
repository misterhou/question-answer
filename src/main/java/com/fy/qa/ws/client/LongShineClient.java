package com.fy.qa.ws.client;

import com.fy.qa.DataCache;
import com.fy.qa.exception.ParamParserException;
import com.fy.utils.JsonUtils;
import com.fy.utils.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class LongShineClient {

    private static Logger log = LoggerFactory.getLogger(LongShineClient.class);

    /**
     * 查询库存金额
     */
    private static final String INVENTORY_INQUIRY_SERVICE_WSDL = "/InventoryInquiryServiceImpl?wsdl";

    private static final String NEW_EGY_SERVER_SERVICE = "/NewEgyServerService?wsdl";

    @Value("${fy.lx.service-url}")
    private String serviceUrl;


    public String smartApiMethod(LongShineCode longShineCode, Map<String, Object> params) {
        String result = null;
        String infoString = JsonUtils.toJsonString(params);
        String wsdlUrl = this.serviceUrl + NEW_EGY_SERVER_SERVICE;
        try {
            Client client = this.getClient(wsdlUrl);
            QName operation = new QName("http://webService.com/", "smartApiMethod");
            log.info("朗新服务地址：{}", wsdlUrl);
            log.info("请求数据：code：{}，infoString：{}", longShineCode, infoString);
            Object[] objects = client.invoke(operation, longShineCode.getValue(), infoString);
            StringBuilder results = new StringBuilder();
            if (objects != null && objects.length > 0) {
                for (Object object : objects) {
                    results.append(object);
                }
            }
            log.info("响应数据: " + results);
            result = results.toString();
        } catch (Exception e) {
            log.error("调用朗新服务异常", e);
        }
        return result;
    }

    /**
     * 库存金额
     * @param question 问题
     * @return 库存金额
     * @throws ParamParserException
     */
    public String inventoryAmount(String question) throws ParamParserException {
        String orgName = DataCache.getCity(question);
//        String startStr = "截止到当前时间";
//        String endStr = "库存金额是多少";
////        String input = "截止到当前时间石家庄库存金额是多少";
//        String regex = startStr + ".*?" + endStr;
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(question);
//        if (matcher.matches()) {
//            orgName = question.replace(startStr, "").replace(endStr, "");
//        }
        if (StringUtils.isEmpty(orgName)) {
            throw new ParamParserException(question, null, "请在问题描述中增加库存所属地区信息");
        }
        String result = null;
        if (StringUtils.hasText(orgName)) {
            String wsdlUrl = this.serviceUrl + INVENTORY_INQUIRY_SERVICE_WSDL;
            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("ROOT");
            Element item = root.addElement("ITEM");
            Element orgNameElement = item.addElement("ORGNAME");
            orgNameElement.setText(orgName);
            String params = root.asXML();
            try {
                log.info("朗新服务地址：{}", wsdlUrl);
                log.info("请求数据：：{}", params);
                Client client = this.getClient(wsdlUrl);
                QName operation = new QName("http://service.inventoryinquiry.api.serving.iscm.ls.com/", "ZMMWTK01_SSKC_001");
                Object[] objects = client.invoke(operation, params);
                Object object = objects[0];
                log.info("响应数据: " + object);
                if (null != object) {
                    String resResult = object.toString();
                    if (resResult.contains("<![CDATA[")) {
                        resResult = resResult.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
                    }
                    Document resDocument = DocumentHelper.parseText(resResult);
                    Element resRootElement = resDocument.getRootElement();
                    List<String> subContent = new ArrayList<>();
                    BigDecimal total = new BigDecimal(0);
                    for (Element resItem : resRootElement.elements()) {
                        StringBuilder content = new StringBuilder();
                        String resOrgName = this.getElementTextTrim(resItem.element("ORGNAME"));
                        content.append(resOrgName);
                        String resKcType = this.getElementTextTrim(resItem.element("KCTYPE"));
                        content.append(resKcType);
                        String kcAmount = this.getElementTextTrim(resItem.element("DMBTR"));
                        content.append(kcAmount);
                        subContent.add(content.toString());
                        if (StringUtils.hasText(kcAmount)) {
                            total = total.add(new BigDecimal(kcAmount.replaceAll("万元", "")));
                        }
                    }
                    String totalAmount = "";
                    BigDecimal temp = total.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_HALF_UP);
                    if (temp.doubleValue() > 1) {
                        totalAmount = temp + "亿元";
                    } else {
                        totalAmount = total + "万元";
                    }
                    result = "您好，截至当前时间" + orgName + "库存为" + totalAmount + "，其中" + String.join("，", subContent);
                }
            } catch (Exception e) {
                log.error("调用朗新服务异常", e);
            }
        } else {
            log.warn("问题参数解析");
        }
        return result;
    }




//    private Client getClient() {
//        if (this.client == null) {
//            JaxWsDynamicClientFactory dynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
////            HashMap<String, Object> properties = new HashMap<>();
////            properties.put("com.sun.xml.ws.connect.timeout", 10 * 1000);
////            properties.put("com.sun.xml.internal.ws.connection.timeout", 10 * 1000);
////            dynamicClientFactory.setJaxbContextProperties(properties);
//            this.client = dynamicClientFactory.createClient(this.wsdlUrl);
//        }
//        return this.client;
//    }

    private Client getClient(String wsdlUrl) {
        JaxWsDynamicClientFactory dynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = dynamicClientFactory.createClient(wsdlUrl);
        return client;
    }

    private String getElementTextTrim(Element element) {
        String text = null;
        if (null != element) {
            text = element.getTextTrim();
        }
        return text;
    }
}
