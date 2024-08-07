package com.fy.qa.ws.client;

/**
 * 朗新方法编号
 */
public enum LongShineCode {

    /**
     * 查询物资信息
     */
    MATERIAL("KCZQ0001"),

    /**
     * 物资的所属仓库信息
     */
    WAREHOUSE_OF_MATERIAL("KCZQ0002"),

    /**
     * 根据专业仓查询物资
     */
    ZYC_MATERIAL("ZYCZQ0001"),

    /**
     * 根据物资查询专业仓信息
     */
    ZYC_WAREHOUSE_OF_MATERIAL_BY_MATERIAL("ZYCZQ0002"),

    /**
     * 根据工厂查询专业仓信息
     */
    ZYC_WAREHOUSE_OF_MATERIAL_BY_FACTORY("ZYCZQ0003");

    private String value;

    LongShineCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
