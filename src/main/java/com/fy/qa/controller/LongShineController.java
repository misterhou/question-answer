package com.fy.qa.controller;

import com.fy.qa.DataCache;
import com.fy.qa.service.IAnswerService;
import com.fy.qa.ws.client.LongShineClient;
import com.fy.qa.ws.client.LongShineCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lx")
@Slf4j
public class LongShineController {

    private LongShineClient longShineClient;

    private IAnswerService answerService;

    public LongShineController(LongShineClient longShineClient, IAnswerService answerService) {
        this.longShineClient = longShineClient;
        this.answerService = answerService;
    }

    @GetMapping("/test")
    public String test() {
        return "梵钰代理朗新的服务";
    }

    @GetMapping("/answer")
    public String getAnswer(String question) {
        String warehouseCode = DataCache.getWarehouseCode(question);
        String materialCode = DataCache.getMaterialCode(question);
//        String answer = "答案是：warehouseCode：" + warehouseCode + ", materialCode：" + materialCode;
        String answer = this.answerService.getAnswer(question);
        return answer;
    }

    @GetMapping("/material")
    public String getMaterial(String warehouseCode, String materialCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("LGORT", warehouseCode);
        params.put("MATNR", materialCode);
        params.put("ZKZBM", "");
        return longShineClient.smartApiMethod(LongShineCode.MATERIAL, params);
    }

    @GetMapping("/warehouse")
    public String getWarehouse(String orgId, String materialCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("WERKS", orgId);
        params.put("MATNR", materialCode);
        params.put("ZKZBM", "");
        return longShineClient.smartApiMethod(LongShineCode.WAREHOUSE_OF_MATERIAL, params);
    }

    @GetMapping("/zyc/material")
    public String getZycMaterial(String warehouseCode) {
        Map<String, Object> params = getParamsOfZYC("LGORT", warehouseCode);
        return longShineClient.smartApiMethod(LongShineCode.ZYC_MATERIAL, params);
    }

    @GetMapping("/zyc/warehouse-by-material")
    public String getZycWarehouseByMaterial(String materialCode) {
        Map<String, Object> params = getParamsOfZYC("MATNR", materialCode);
        return longShineClient.smartApiMethod(LongShineCode.ZYC_WAREHOUSE_OF_MATERIAL_BY_MATERIAL, params);
    }

    @GetMapping("/zyc/warehouse-by-factory")
    public String getZycWarehouseByFactory(String factoryCode) {
        Map<String, Object> params = getParamsOfZYC("WERKS", factoryCode);
        return longShineClient.smartApiMethod(LongShineCode.ZYC_WAREHOUSE_OF_MATERIAL_BY_FACTORY, params);
    }

    private static Map<String, Object> getParamsOfZYC(String paramKey, String paramValue) {
        List<Map<String, Object>> result = null;
        if (paramValue == null) {
            result = Collections.emptyList();
        } else {
            result = Arrays.stream(paramValue.split(",")).map(s -> {
                Map<String, Object> params = new HashMap<>();
                params.put(paramKey, s);
                return params;
            }).collect(Collectors.toList());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("result", result);
        return params;
    }
}
