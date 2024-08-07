package com.fy;

import com.fy.qa.DataCache;
import com.fy.qa.DataCacheFileGenerator;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class Startup {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Startup.class, args);
        Environment environment = applicationContext.getEnvironment();
        String warehouseDataCacheFile = environment.getProperty("fy.lx.warehouse-data-file");
        String materialDataCacheFile = environment.getProperty("fy.lx.material-data-file");
        DataCache.loadData(warehouseDataCacheFile, materialDataCacheFile);
//        generateCommonData();
//        dom4j();
//        createXML();
//        parseXML();
//        testRegex();
    }


    private static void generateCommonData() {
        DataCacheFileGenerator.generateWarehouseDataCache("src/main/resources/库存数据字典.xlsx");
        DataCacheFileGenerator.generateMaterialDataCache("src/main/resources/库存数据字典.xlsx");
    }

    /**
     * xml文件路径
     */
    private static final String XML_PATH = "books.xml";

    public static void dom4j() {
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件, 获取Document对象。
            Document document = reader.read(new File(XML_PATH));
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历某一本书=====");
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    System.out.println("属性名：" + attr.getName() + "--属性值：" + attr.getValue());
                }
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                }
                System.out.println("=====结束遍历某一本书=====");
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void createXML() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("ROOT");
        Element item = root.addElement("ITEM");
        Element orgName = item.addElement("ORGNAME");
        orgName.setText("石家庄");
        System.out.println(document.asXML());
    }

    private static void parseXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
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
                "</ROOT>";
        try {
            Document document = DocumentHelper.parseText(xml);
            // 通过document对象获取根节点bookstore
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历某一本书=====");
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    System.out.println("属性名：" + attr.getName() + "--属性值：" + attr.getValue());
                }
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    System.out.println("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                }
                System.out.println("=====结束遍历某一本书=====");
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private static void testRegex() {
        String startStr = "截止到当前时间";
        String endStr = "库存金额是多少";
        String input = "截止到当前时间石家庄库存金额是多少";
        String regex = startStr + ".*?" + endStr;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String param = input.replace(startStr, "").replace(endStr, "");
            System.out.println(param);
        }
    }
}
