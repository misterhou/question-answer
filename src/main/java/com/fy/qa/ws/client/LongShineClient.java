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
import org.springframework.util.ObjectUtils;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LongShineClient {

    private static Logger log = LoggerFactory.getLogger(LongShineClient.class);

    /**
     * 查询库存金额
     */
    private static final String INVENTORY_INQUIRY_SERVICE_WSDL = "/InventoryInquiryServiceImpl?wsdl";

    /**
     * 单据签署
     */
    private static final String BILL_SIGN_SERVICE_WSDL = "/BillSignServiceImpl?wsdl";

    /**
     * 预警生成情况
     */
    private static final String WARING_INFO_SERVICE_WSDL = "/WarningInfoServiceImpl?wsdl";

    private static final String NEW_EGY_SERVER_SERVICE = "/NewEgyServerService?wsdl";

    @Value("${fy.lx.service-url}")
    private String serviceUrl;

    /**
     * 库存情况
     * @param longShineCode 编码
     * @param params 参数
     * @return 库存数据
     */
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
     * @throws ParamParserException 当参数解析出错时会抛出此异常
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
            throw new ParamParserException("请在问题描述中增加地区信息", question);
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
                QName operation = new QName("http://service.inventoryinquiry.api.serving.iscm.ls.com/", "ZMMWTK01_SSKC_001");
                Element resRootElement = this.getResponse(wsdlUrl, operation, params);
//                log.info("朗新服务地址：{}", wsdlUrl);
//                log.info("请求数据：：{}", params);
//                Client client = this.getClient(wsdlUrl);
//                Object[] objects = client.invoke(operation, params);
//                Object object = objects[0];
//                log.info("响应数据: " + object);
//                if (null != object) {
//                    String resResult = object.toString();
//                    if (resResult.contains("<![CDATA[")) {
//                        resResult = resResult.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
//                    }
//                Document resDocument = DocumentHelper.parseText(resResult);
//                Element resRootElement = resDocument.getRootElement();
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
//                }
            } catch (Exception e) {
                log.error("解析响应数据错误", e);
            }
        } else {
            log.warn("问题参数解析");
        }
        return result;
    }

    /**
     * 单据签署情况统计
     * @param question 问题
     * @return 答案
     * @throws ParamParserException 当参数解析出错时会抛出此异常
     */
    public String billSign(String question) throws ParamParserException {
        String result = null;
        String params = getOrgNameAndStartTimeAndEndTime(question);
        String wsdlUrl = this.serviceUrl + BILL_SIGN_SERVICE_WSDL;
        QName operation = new QName("http://service.inventoryinquiry.api.serving.iscm.ls.com/", "ZMMWTK01_DJTJ_002");
        Element rootElement = this.getResponse(wsdlUrl, operation, params);
        if (null != rootElement) {
            StringBuilder content = new StringBuilder();
            Element head = rootElement.element("HEAD");
            Element resOrgName = head.element("ORGNAME");
            content.append("您好，").append(this.getElementTextTrim(resOrgName));
            Element scBillSum = head.element("SCBILLSUM");
            content.append("共生成").append(this.getElementTextTrim(scBillSum));
            Element qsBillSum = head.element("QSBILLSUM");
            content.append("，已完成签署").append(this.getElementTextTrim(qsBillSum));
            List<Element> itemList = rootElement.elements("ITEM");
            if (!ObjectUtils.isEmpty(itemList)) {
                List<String> subItemList = new ArrayList<>();
                for (Element element : itemList) {
                    Element billType = element.element("BILLTYPE");
                    Element billSum = element.element("BILLSUM");
                    subItemList.add(this.getElementTextTrim(billType) + this.getElementTextTrim(billSum));
                }
                if (!ObjectUtils.isEmpty(subItemList)) {
                    content.append("，其中").append(String.join("，", subItemList));
                }
            }
            result = content.toString();
        }
        return result;
    }

    /**
     * 预警生成情况
     * @param question 问题
     * @return 答案
     * @throws ParamParserException 当参数解析出错时会抛出此异常
     */
    public String waringInfo(String question) throws ParamParserException {
        String result = "您好，2024年全省生成XX条预警信息，其中红色告警XX条，黄色预警XX条，蓝色提醒XX条。";
        String params = getOrgNameAndStartTimeAndEndTime(question);
        String wsdlUrl = this.serviceUrl + WARING_INFO_SERVICE_WSDL;
        QName operation = new QName("http://service.inventoryinquiry.api.serving.iscm.ls.com/", "ZMMWTK01_YJTJ_003");
        Element rootElement = this.getResponse(wsdlUrl, operation, params);
        if (null != rootElement) {
            StringBuilder content = new StringBuilder();
            content.append("您好，");
            Element head = rootElement.element("HEAD");
            Element orgName = head.element("ORGNAME");
            content.append(this.getElementTextTrim(orgName));
            Element warnHzSum = head.element("WARNHZSUM");
            content.append("生成").append(this.getElementTextTrim(warnHzSum)).append("预警信息");
            List<Element> itemList = rootElement.elements("ITEM");
            if (!ObjectUtils.isEmpty(itemList)) {
                List<String> subItem = new ArrayList<>();
                for (Element item : itemList) {
                    Element warnLevel = item.element("WARNLEVEL");
                    Element warnSum = item.element("WARNSUM");
                    subItem.add(this.getElementTextTrim(warnLevel) + this.getElementTextTrim(warnSum));
                }
                content.append("，其中").append(String.join("，", subItem));
            }
            result = content.toString();
        }
        return result;
    }

    /**
     * 获取地区、开始日期、结束日期 参数
     * @param question 问题
     * @return 参数数据（XML格式）
     * @throws ParamParserException 当参数解析出错时会抛出此异常
     */
    private static String getOrgNameAndStartTimeAndEndTime(String question) throws ParamParserException {
        String orgName = DataCache.getCity(question);
        if (StringUtils.isEmpty(orgName)) {
            throw new ParamParserException("请在问题描述中增加地区信息", question);
        }
        List<String> monthStr = DataCache.getMonthString(question);
        if (ObjectUtils.isEmpty(monthStr)) {
            throw new ParamParserException("请在问题描述中增加时间信息", question);
        }
        String startDate = monthStr.get(0);
        String endDate = monthStr.get(monthStr.size()-1);
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("ROOT");
        Element item = root.addElement("ITEM");
        Element orgNameElement = item.addElement("ORGNAME");
        orgNameElement.setText(orgName);
        Element starTime = item.addElement("STARTIME");
        starTime.setText(startDate);
        Element endTime = item.addElement("ENDTIME");
        endTime.setText(endDate);
        String params = root.asXML();
        return params;
    }

    /**
     * 获取 webservice 响应数据（数据格式为 XML）
     * @param wsdlUrl webservice 地址
     * @param operation 方法
     * @param params 参数
     * @return 响应数据
     */
    private Element getResponse(String wsdlUrl, QName operation, String params) {
        Element resRootElement = null;
        try {
            log.info("朗新服务地址：{}", wsdlUrl);
            log.info("请求数据：：{}", params);
            Client client = this.getClient(wsdlUrl);
            Object[] objects = client.invoke(operation, params);
            Object object = objects[0];
            log.info("响应数据: " + object);
            if (null != object) {
                String result = object.toString();
                if (result.contains("<![CDATA[")) {
                    result = result.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
                }
                Document resDocument = DocumentHelper.parseText(result);
                resRootElement = resDocument.getRootElement();
            }
        } catch (Exception e) {
            log.error("调用朗新服务异常", e);
        }
        return resRootElement;
    }

    /**
     * 获取 webservice 客户端
     * @param wsdlUrl webservice 地址
     * @return webservice 客户端
     */
    private Client getClient(String wsdlUrl) {
        JaxWsDynamicClientFactory dynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = dynamicClientFactory.createClient(wsdlUrl);
        return client;
    }

    /**
     * 获取 XML 节点值
     * @param element XML 节点
     * @return XML 节点值
     */
    private String getElementTextTrim(Element element) {
        String text = null;
        if (null != element) {
            text = element.getTextTrim();
        }
        return text;
    }
}
