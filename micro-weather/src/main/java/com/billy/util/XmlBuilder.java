package com.billy.util;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * xml 读取工具类
 * @author BillyYang
 *
 */
public class XmlBuilder {

	public static Object xmStr2Object(Class<?> clazz, String xmlStr) throws Exception {
		
		Object result = null;
		Reader reader = null;
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		reader = new StringReader(xmlStr);
		result = unmarshaller.unmarshal(reader);
		
		return result;
	}
}
