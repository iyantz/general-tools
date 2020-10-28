package cn.yantz.xmltools;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * XML文件读取帮助类
 * @author cn.yantz@qq.com
 * @version 1.0
 * @link GeneralTools cn.yantz.xmltools
 * @since 2020/10/28 15:15
 */
public class XmlReaderUtils {

    /**
     * 读取XMl文件流
     *
     * @param xmlString xml字符串
     * @return Map<String, Object>
     */
    public Map<String, Object> xmlToMap(String xmlString) throws DocumentException {
        Document document = org.dom4j.DocumentHelper.parseText(xmlString);
        Element rootElement = document.getRootElement();
        Map<String, Object> resultMap = new HashMap<>(16);
        readXmlDocument(rootElement, resultMap);
        return resultMap;
    }

    public Map<String, Object> xmlToMap(FileInputStream xmlInputStream){
        //todo 待实现
        return null;
    }


    public String xmlToJsonString(String xmlString){
        //todo 待实现
        return "";
    }

    private void readXmlDocument(Element element, Map<String, Object> resultMap) {
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element nextElement = (Element) iterator.next();
            resultMap.put(nextElement.getName(), nextElement.getTextTrim());
            readXmlDocument(nextElement, resultMap);
        }
    }

    private static volatile XmlReaderUtils xmlReaderUtils = null;

    public static XmlReaderUtils getInstance() {
        if (xmlReaderUtils == null) {
            synchronized (XmlReaderUtils.class) {
                if (xmlReaderUtils == null) {
                    xmlReaderUtils = new XmlReaderUtils();
                }
            }
        }
        return xmlReaderUtils;
    }

    private XmlReaderUtils() {

    }
}
