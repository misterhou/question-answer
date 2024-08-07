package com.fy.qa;

import com.fy.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;


@Slf4j
public class DataCacheFileGenerator {

    /**
     * 生成仓库缓存数据
     * @param excelFile excel 文件名
     */
    public static void generateWarehouseDataCache(String excelFile) {
        try {
            Map<String, String> data = ExcelUtils.getData(excelFile,
                    1, "wh_no", 1);
            String cacheFile = "WarehouseDataCache";
            writeObject2File(data, cacheFile);
        } catch (Exception e) {
            log.error("生成仓库缓存文件出错", e);
        }
    }

    public static void generateMaterialDataCache(String excelFile) {
        try {
            Map<String, String> data = ExcelUtils.getData(excelFile,
                    0, "物料编码", 1);
            String cacheFile = "MaterialDataCache";
            writeObject2File(data, cacheFile);
        } catch (Exception e) {
            log.error("生成物资缓存文件出错", e);
        }
    }


    /**
     * 将对象写入文件
     *
     * @param obj 待写入的对象
     * @param file 待写入的文件
     * @throws IOException 将对象写入文件报错时，会抛出此异常
     */
    private static void writeObject2File(Object obj, String file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(obj);
        outputStream.close();
    }
}
