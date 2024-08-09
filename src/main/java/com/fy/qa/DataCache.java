package com.fy.qa;

import com.fy.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DataCache {

    private static Map<String, String> warehouse = new LinkedHashMap<>(1500);

    private static Map<String, String> material = new LinkedHashMap<>(150000);

    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    /**
     * 从文本中提取日期，返回数据格式为 yyyy-MM-dd
     * @param message 文本信息
     * @return 日期集合
     */
    public static List<String> getMonthString(String message) {
        List<String> result = null;
        LocalDate now = LocalDate.now();
        String currentYearRegex = "(本年|当年|当前年)";
        String currentMonthRegex = "(本月|当月|当前月)";
        String monthRegex = "(0?[1-9]|1[0-2])月份";
        String beforeMonthRegex = "(上一个月|上月)";
        String afterMonthRegex = "(下一个月|下月)";
        String singleYearRegex = "\\d{4}年";
        if (StringUtils.regexIsFind(currentYearRegex, message) || StringUtils.regexIsFind(singleYearRegex, message)) {
            int year = now.getYear();
            List<String> yearRegexValue = getRegexValue(singleYearRegex, message);
            if (!ObjectUtils.isEmpty(yearRegexValue)) {
                String yearStr = yearRegexValue.get(0);
                year = Integer.valueOf(yearStr.replaceAll("年", ""));
            }
            String startDate = year + "-01-01";
            String endDate = year + "-12-31";
            result = Arrays.asList(startDate, endDate);
        } else if (StringUtils.regexIsFind(currentMonthRegex, message)) {
            LocalDate startLocalDate = now.withDayOfMonth(1);
//            String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String startDate = startLocalDate.format(YYYY_MM_DD);
            LocalDate endLocalDate = getLastDayOfMonth(startLocalDate);
            String endDate = endLocalDate.format(YYYY_MM_DD);
            result = Arrays.asList(startDate, endDate);
        } else if (StringUtils.regexIsFind(beforeMonthRegex, message)) {
            LocalDate startLocalDate = now.minusMonths(1).withDayOfMonth(1);
            LocalDate endLocalDate = getLastDayOfMonth(startLocalDate);
            String startDate = startLocalDate.format(YYYY_MM_DD);
            String endDate = endLocalDate.format(YYYY_MM_DD);
            result = Arrays.asList(startDate, endDate);
        } else if (StringUtils.regexIsFind(afterMonthRegex, message)) {
            LocalDate startLocalDate = now.plusMonths(1).withDayOfMonth(1);
            LocalDate endLocalDate = getLastDayOfMonth(startLocalDate);
            String startDate = startLocalDate.format(YYYY_MM_DD);
            String endDate = endLocalDate.format(YYYY_MM_DD);
            result = Arrays.asList(startDate, endDate);
        } else if (StringUtils.regexIsFind(monthRegex, message)) {
            List<String> regexValue = getRegexValue(monthRegex, message);
            if (!ObjectUtils.isEmpty(regexValue)) {
                String month = regexValue.get(0);
                int monthInt = Integer.valueOf(month.replaceAll("月份", ""));
                int yearInt = now.getYear();
                String yearRegex = "\\d{4}年" + monthRegex;
                List<String> yearRegexValue = getRegexValue(yearRegex, message);
                if (!ObjectUtils.isEmpty(yearRegexValue)) {
                    String year = yearRegexValue.get(0);
                    yearInt = Integer.valueOf(year.replaceAll("年" + month, ""));
                }
                LocalDate startLocalDate = LocalDate.of(yearInt, monthInt, 1);
                LocalDate endLocalDate = getLastDayOfMonth(startLocalDate);
                String startDate = startLocalDate.format(YYYY_MM_DD);
                String endDate = endLocalDate.format(YYYY_MM_DD);
                result = Arrays.asList(startDate, endDate);
            }
        }
        return result;
    }

    /**
     * 提取文本中日期，只能提取年月日
     * @param message 文本
     * @return 日期集合
     */
    public static List<String> getYearMonthDateString(String message) {
        String yearAndMonthRegex = "\\d{4}[\\.-年]\\d{1,2}[\\.-月]\\d{1,2}[日]{0,1}";
        List<String> result = getRegexValue(yearAndMonthRegex, message);
        return result;
    }

    /**
     * 获取正则表达式匹配到的文本
     * @param regex 正则表达式
     * @param text 待匹配的文本
     * @return 匹配到的文本
     */
    private static List<String> getRegexValue(String regex, String text) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 获取指定日期所属月份的最后一天日期
     * @param localDate 日期
     * @return 指定月份的最后一天
     */
    private static LocalDate getLastDayOfMonth(LocalDate localDate) {
        // 将日期调整为1号，增加一个月，减1天
        return localDate.withDayOfMonth(1).plusMonths(1).plusDays(-1);
    }
}
