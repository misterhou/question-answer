package com.fy.qa;

import com.fy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataCache {

    private static Map<String, String> warehouse = new LinkedHashMap<>(1500);

    private static Map<String, String> material = new LinkedHashMap<>(150000);

    private static List<String> city = Arrays.asList("全省", "雄安", "石家庄", "保定", "衡水", "邢台", "邯郸", "沧州");

    public static void loadData(String warehouseDataFile, String materialDataFile) {
        try {
            warehouse = loadData(warehouseDataFile);
            material = loadData(materialDataFile);
        } catch (Exception e) {
            log.error("加载数据文件出错", e);
        }
    }

    public static String getWarehouseCode(String message) {
        return getValue(message, warehouse);
    }

    public static String getMaterialCode(String message) {
        return getValue(message, material);
    }

    public static String getValue(String message, Map<String, String> data) {
        String value = null;
        for (String key : data.keySet()) {
            if (message.contains(key)) {
                value = data.get(key);
                break;
            }
        }
        return value;
    }

    /**
     * 加载缓存文件
     *
     * @param commandCacheFile 缓存文件
     * @return 缓存 map
     * @throws IOException 配置文件读取出错，会抛出此异常
     * @throws ClassNotFoundException 配置文件数据有问题，会抛出此异常
     */
    public static Map<String, String> loadData(String commandCacheFile) throws IOException,
            ClassNotFoundException {
        Map<String, String> data = null;
        log.info("开始加载缓存文件：{}", commandCacheFile);
        ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(Paths.get(commandCacheFile)));
        data = (Map<String, String>) inputStream.readObject();
        if (log.isDebugEnabled()) {
            log.debug("缓存文件数据：{}", data);
        }
        log.info("图片数量：{}", data.size());
        return data;
    }

    /**
     * 获取城市信息
     * @param message 消息
     * @return 消息中的程序
     */
    public static String getCity(String message) {
        String cityName = null;
        for (String temp : city) {
            if (StringUtils.regexIsFind(temp, message)) {
                cityName = temp;
                break;
            }
        }
        return cityName;
    }
}
