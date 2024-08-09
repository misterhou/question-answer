package com.fy.qa.ws.imitate.impl;

import com.fy.qa.ws.imitate.INewEgyServerService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@Component
@WebService(serviceName = "INewEgyServerServiceService", targetNamespace = "http://webService.com/")
public class INewEgyServerServiceImpl implements INewEgyServerService {


    @Override
    public String smartApiMethod(String code, String infoString) {
        return "{\n" +
                "    \"msg\": \"新一代库存物资查询增强接口一获取成功\",\n" +
                "    \"key\": \"true\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"LGORT\": \"91D1\",\n" +
                "            \"LGOBE\": \"常州项目现场库\",\n" +
                "            \"WLDL\": \"G10\",\n" +
                "            \"WGBEZ1\": \"一次设备\",\n" +
                "            \"WLZL\": \"G1012\",\n" +
                "            \"WGBEZ2\": \"电抗器\",\n" +
                "            \"MATKL\": \"G1012002\",\n" +
                "            \"WGBEZ\": \"并联电抗器\",\n" +
                "            \"MATNR\": \"500000001\",\n" +
                "            \"MAKTX\": \"35KV单相干式一体式并联电抗器,20000KVAR1\",\n" +
                "            \"ZKZBM\": \"\",\n" +
                "            \"ZKZMST\": \"\",\n" +
                "            \"SL\": \"498.000\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"LGORT\": \"91D1\",\n" +
                "            \"LGOBE\": \"常州项目现场库\",\n" +
                "            \"WLDL\": \"G10\",\n" +
                "            \"WGBEZ1\": \"一次设备\",\n" +
                "            \"WLZL\": \"G1012\",\n" +
                "            \"WGBEZ2\": \"电抗器\",\n" +
                "            \"MATKL\": \"G1012002\",\n" +
                "            \"WGBEZ\": \"并联电抗器\",\n" +
                "            \"MATNR\": \"500000001\",\n" +
                "            \"MAKTX\": \"35KV单相干式一体式并联电抗器,20000KVAR1\",\n" +
                "            \"ZKZBM\": \"\",\n" +
                "            \"ZKZMST\": \"\",\n" +
                "            \"SL\": \"505.000\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
