package com.kyee.monitor.base.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.internal.framework.FrameworkInternalSystemException;
import com.kyee.monitor.base.common.util.CommonUtils.DateUtil.DATE_PATTERN;
import com.kyee.monitor.base.common.util.sql_formatter.impl.DDLFormatter;
import com.kyee.monitor.base.common.util.sql_formatter.impl.DMLFormatter;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;


/**
 * @author
 */
public class CommonUtils {

	private static Log logger = LogFactory.getLog(CommonUtils.class);

	private CommonUtils(){
	}
	
	
	
	/**
	 * 集合工具类
	 */
	public static class CollectionsUtil{
		
		/**
		 * 将查询结果集转换为 List 对象
		 * 
		 * @param queryResult
		 * @param columnName
		 * @return
		 */
		public static List<Object> table2List(List<Map<String, Object>> queryResult, String columnName){
			
			List<Object> rs = new ArrayList<Object>();
			
			for(Map<String, Object> record : queryResult){
				
				String value = record.get(columnName) + "";
				
				rs.add(value);
			}
			
			return rs;
		}
		
		/**
		 * 将查询结果集转换为 Map 对象
		 * 
		 * @param queryResult
		 * @param keyColumnName
		 * @param valueColumnName
		 * @return
		 */
		public static Map<String, Object> table2Map(List<Map<String, Object>> queryResult, String keyColumnName, String valueColumnName){
			
			Map<String, Object> rs = new HashMap<String, Object>();
			
			for(Map<String, Object> record : queryResult){
				
				String key = record.get(keyColumnName) + "";
				Object value = record.get(valueColumnName);
				
				rs.put(key, value);
			}
			
			return rs;
		}
		
		/**
		 * 将查询结果集转换为矩阵对象
		 * 
		 * @param queryResult
		 * @param columnNames
		 * @return
		 */
		public static List<List<Object>> table2Matrix(List<Map<String, Object>> queryResult, String[] columnNames){
			
			List<List<Object>> rs = new LinkedList<List<Object>>();
			
			for(Map<String, Object> record : queryResult){
				
				List<Object> item = new LinkedList<Object>();
				for(String col : columnNames){
					
					if(record.containsKey(col)){
						
						item.add(record.get(col));
					}
				}
				
				rs.add(item);
			}
			
			return rs;
		}
		
		/**
		 * 移除集合中重复的记录
		 * <br/>
		 * 该方法用于删除不同 Map 对象相同值序列的记录，并且总是保证保留重复记录的第一条，
		 * 删除后续的其他重复记录，并保证原始集合顺序
		 * 
		 * @param list
		 * @return
		 */
		public static List<Map<String, Object>> removeRepeat(List<Map<String, Object>> list){
			
			List<Map<String, Object>> listTmp = new ArrayList<Map<String,Object>>();
			List<String> recordSerStrTmp = new ArrayList<String>();
			for(Map<String, Object> record : list){
				
				String recordSer = "";
				for(Map.Entry<String, Object> entry : record.entrySet()){
					
					recordSer += entry.getValue().hashCode();
				}
				if(recordSerStrTmp.contains(recordSer)){
					
					continue;
				}else{
					
					recordSerStrTmp.add(recordSer);
				}
				
				listTmp.add(record);
			}
			
			return listTmp;
		}
	}
	
	
	
	
	
	/**
	 * 日期时间工具类
	 */
	public static class DateUtil{
		
		/**
		 * 日期格式
		 */
		public enum DATE_PATTERN{
			
			/**
			 * yyyyMMdd
			 */
			yyyyMMdd("yyyyMMdd", "^\\d{2,4}\\d{1,2}\\d{1,2}$"),
						
			/**
			 * yyyy/MM
			 */
			yyyy_MM("yyyy/MM", "^\\d{2,4}/\\d{1,2}$"),
			
			/**
			 * yyyy-MM
			 */
			yyyy_MM2("yyyy-MM", "^\\d{2,4}-\\d{1,2}$"),
			
			/**
			 * yyyy/MM/dd
			 */
			yyyy_MM_dd("yyyy/MM/dd", "^\\d{2,4}/\\d{1,2}/\\d{1,2}$"),
			
			/**
			 * yyyy-MM-dd
			 */
			yyyy_MM_dd2("yyyy-MM-dd", "^\\d{2,4}-\\d{1,2}-\\d{1,2}$"),
			
			/**
			 * yyyy/MM/dd HH:mm
			 */
			yyyy_MM_dd_HH_mm("yyyy/MM/dd HH:mm", "^\\d{2,4}/\\d{1,2}/\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}$"),
			
			/**
			 * yyyy-MM-dd HH:mm
			 */
			yyyy_MM_dd_HH_mm2("yyyy-MM-dd HH:mm", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}$"),
			
			/**
			 * yyyy/MM/dd HH:mm:ss
			 */
			yyyy_MM_dd_HH_mm_ss("yyyy/MM/dd HH:mm:ss", "^\\d{2,4}/\\d{1,2}/\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}$"),
			
			/**
			 * yyyy-MM-dd HH:mm:ss
			 */
			yyyy_MM_dd_HH_mm_ss2("yyyy-MM-dd HH:mm:ss", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}$"),
			
			/**
			 * yyyy/MM/dd HH:mm:ss.S
			 */
			yyyy_MM_dd_HH_mm_ss_S("yyyy/MM/dd HH:mm:ss.S", "^\\d{2,4}/\\d{1,2}/\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1}$"),
			
			/**
			 * yyyy-MM-dd HH:mm:ss.S
			 */
			yyyy_MM_dd_HH_mm_ss_S2("yyyy-MM-dd HH:mm:ss.S", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1}$"),
			
			/**
			 * yyyy/MM/dd HH:mm:ss.SS
			 */
			yyyy_MM_dd_HH_mm_ss_SS("yyyy/MM/dd HH:mm:ss.SS", "^\\d{2,4}/\\d{1,2}/\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{2}$"),
			
			/**
			 * yyyy-MM-dd HH:mm:ss.SS
			 */
			yyyy_MM_dd_HH_mm_ss_SS2("yyyy-MM-dd HH:mm:ss.SS", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{2}$"),
			
			
			/**
			 * yyyy/MM/dd HH:mm:ss.SSS
			 */
			yyyy_MM_dd_HH_mm_ss_SSS("yyyy/MM/dd HH:mm:ss.SSS", "^\\d{2,4}/\\d{1,2}/\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3,}$"),
			
			/**
			 * yyyy-MM-dd HH:mm:ss.SSS
			 */
			yyyy_MM_dd_HH_mm_ss_SSS2("yyyy-MM-dd HH:mm:ss.SSS", "^\\d{2,4}-\\d{1,2}-\\d{1,2}\\s.{1,2}\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{3,}$");
			
			
			
			private String value;
			
			private String pattern;
			
			private DATE_PATTERN(String value, String pattern){
				this.value = value;
				this.pattern = pattern;
			}
			
			/**
			 * 根据样本获取模式
			 * 
			 * @param date
			 * @return
			 * @throws Exception 
			 */
			public static DATE_PATTERN getPatternBySample(String date) throws Exception{
				
				if(date != null){
					
					date = date.trim();
					
					for(DATE_PATTERN value : DATE_PATTERN.values()){
					
						if(date.matches(value.pattern)){
							return value;
						}
					}
				}
				
				throw new Exception("日期为空或是不支持的样本格式：" + date);
			}
			
			@Override
			public String toString() {
				return value;
			}
		}
		
		/**
		 * 日期比较
		 * 
		 * @param date1
		 * @param date2
		 * @return 如果 date1 > date2，则返回 1;如果 date1 = date2,则返回 0;如果 date1 < date2，则返回 -1
		 */
		public static int compareDate(Date date1, Date date2){
			
			if(date1.getTime() > date2.getTime()){
				
				return 1;
			}else if(date1.getTime() == date2.getTime()){
				
				return 0;
			}else{
				
				return -1;
			}
		}
		
		/**
		 * 日期比较
		 * 
		 * @param date1
		 * @param date2
		 * @return 如果 date1 > date2，则返回 1;如果 date1 = date2,则返回 0;如果 date1 < date2，则返回 -1
		 * @throws Exception 
		 */
		public static int compareDate(String date1, DATE_PATTERN date1Pattern, String date2, DATE_PATTERN date2Pattern) throws Exception{
			
			Date _date1 = parseDate(date1, date1Pattern);
			Date _date2 = parseDate(date2, date2Pattern);
			
			return compareDate(_date1, _date2);
		}
		
		/**
		 * 根据指定的模式，将 java.util.Date 转换为 java.sql.Date
		 * 
		 * @param date
		 * @param datePattern
		 * @return
		 * @throws Exception 
		 */
		public static java.sql.Date toSqlDate(String date, DATE_PATTERN datePattern) throws Exception{
			
			return new java.sql.Date(parseDate(date, datePattern).getTime());
		}
		
		/**
		 * 根据指定的模式，将 java.util.Date 转换为 java.sql.Timestamp
		 * 
		 * @param date
		 * @param datePattern
		 * @return
		 * @throws Exception 
		 */
		public static Timestamp toTimestamp(String date, DATE_PATTERN datePattern) throws Exception{
			
			return new Timestamp(parseDate(date, datePattern).getTime());
		}
		
		/**
		 * 解析任意受支持格式的时间
		 * 
		 * @param date
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("deprecation")
		public static Date parseDate(String date) throws Exception{
			
			if(date == null){
				return null;
			}
			
			date = date.trim();
			if(date.indexOf("中国标准时间") != -1 || date.indexOf("CST") != -1){
				return new Date(Date.parse(date));
			}
			
			return parseDate(date, getPatternBySample(date));
		}
		
		/**
		 * 根据指定模式解析时间字符串为 date 对象并返回
		 * 
		 * @param date date字符串
		 * @param pattern 模式
		 * @return
		 * @throws Exception 
		 */
		public static Date parseDate(String date, DATE_PATTERN pattern) throws Exception{
			
			try {
				
				if(pattern == DATE_PATTERN.yyyy_MM_dd_HH_mm_ss_SSS || pattern == DATE_PATTERN.yyyy_MM_dd_HH_mm_ss_SSS2){
					if (date != null){						
						int index = date.lastIndexOf(".");
						if (index != -1) {						
							date = date.substring(0, index + 4);
						}
					}
				}				
				
				DateFormat df = new SimpleDateFormat(pattern.toString());
				return df.parse(date);
				
			} catch (Exception e) {
				throw e;
			}
		}
		
		/**
		 * 格式化时间
		 * <br/>
		 * 按照指定模式格式化时间字符串
		 * 
		 * @param srcDateStr 源日期
		 * @param srcDatePattern 源日期模式
		 * @param targetDatePattern 目标日期模式
		 * @return
		 * @throws Exception 
		 */
		public static String formatDate(String srcDateStr, DATE_PATTERN srcDatePattern, DATE_PATTERN targetDatePattern) throws Exception{
			
			try {
				
				Date srcDate = parseDate(srcDateStr, srcDatePattern);
				
				DateFormat df2 = new SimpleDateFormat(targetDatePattern.toString());
				String targetDateStr = df2.format(srcDate);
				
				return targetDateStr;
				
			} catch (Exception e) {
				throw e;
			}
		}
		
		/**
		 * 格式化时间
		 * 
		 * @param date
		 * @param datePattern 模式
		 * @return
		 */
		public static String formatDate(Date date, DATE_PATTERN datePattern){
			
			DateFormat df = new SimpleDateFormat(datePattern.toString());
			return df.format(date);
		}
		
		/**
		 * 根据日期样品获得其模式
		 * 
		 * @param sample
		 * @return
		 * @throws Exception 
		 */
		public static DATE_PATTERN getPatternBySample(String sample) throws Exception{
			
			return DATE_PATTERN.getPatternBySample(sample);
		}
	}
	
	
	
	/**
	 * JSON 工具类
	 */
	public static class JsonUtil{
		
		private static Gson gson;
		
		static {
			
			GsonBuilder builder = new GsonBuilder();
			builder.setDateFormat(DATE_PATTERN.yyyy_MM_dd_HH_mm_ss.toString());
			builder.setExclusionStrategies(new GsonExclusionStrategy());
			gson = builder.serializeNulls().create();
		}
		
		/**
		 * 获取 gson 对象
		 * 
		 * @return
		 */
		public static Gson getGson(){
			
			return gson;
		}
		
		/**
		 * 将 JSON 转换为 Map
		 * 
		 * @param jsonSrc
		 * @return
		 */
		public static Map<String, Object> json2Map(String jsonSrc){
			
			@SuppressWarnings("unchecked")
			Map<String, Object> rs = gson.fromJson(jsonSrc, Map.class);
			
			return rs;
		}
		
		/**
		 * 将 JSON 转换为 Map
		 * 
		 * @param reader
		 * @return
		 */
		public static Map<String, Object> json2Map(Reader reader){
			
			@SuppressWarnings("unchecked")
			Map<String, Object> rs = gson.fromJson(reader, Map.class);
			
			return rs;
		}
		
		/**
		 * 将 JSON 转换为 List
		 * 
		 * @param jsonSrc
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public static List json2List(String jsonSrc){
			
			return gson.fromJson(jsonSrc, List.class);
		}
		
		/**
		 * 将 JSON 转换为 List
		 * 
		 * @param reader
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public static List json2List(Reader reader){
			
			return gson.fromJson(reader, List.class);
		}
		
		/**
		 * 将 JSON 转换为任意对象
		 * 
		 * @param reader
		 * @param type 可使用 TypeToken 获得，示例：new TypeToken<List<DataBean>>(){}.getType()
		 * @return
		 */
		public static <T> T json2AnyType(Reader reader, Type type){
			
			return gson.fromJson(reader, type);
		} 
		
		/**
		 * 将 JSON 转换为任意对象
		 *
		 * @param jsonSrc
		 * @param type 可使用 TypeToken 获得，示例：new TypeToken<List<DataBean>>(){}.getType()
		 * @return
		 */
		public static <T> T json2AnyType(String jsonSrc, Type type){

			return gson.fromJson(jsonSrc, type);
		}

		/**
		 * 将 JSON 转换为对象
		 * 
		 * @param jsonSrc
		 * @param type
		 * @return
		 */
		public static <T> T json2Object(String jsonSrc, Class<T> type){
			
			return gson.fromJson(jsonSrc, type);
		}
		
		/**
		 * 将 JSON 转换为对象
		 * 
		 * @param reader
		 * @param type
		 * @return
		 */
		public static <T> T json2Object(Reader reader, Class<T> type){
			
			return gson.fromJson(reader, type);
		}
		
		
		/**
		 * 根据 key 获取 value
		 * 
		 * @param <T>
		 * @param jsonSrc
		 * @param key
		 * @param type
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static <T> T getValue(String jsonSrc, String key, Class<T> type){
			
			return (T)json2Map(jsonSrc).get(key);
		}
		
		/**
		 * 将 java 对象映射为 json str
		 * 
		 * @param obj
		 * @return
		 */
		public static String object2Json(Object obj){
			
			return gson.toJson(obj);
		}
		
		/**
		 * 判断是否为合法的 json
		 * 
		 * @param jsonSrc
		 * @return
		 */
		public static boolean validate(String jsonSrc){
			
			try {
				gson.fromJson(jsonSrc, Object.class);
			} catch (JsonSyntaxException e) {
				return false;
			}
			
			return true;
		}
	}
	
	
	/**
	 * 文本工具类
	 */
	public static class TextUtil{
		/**
		 * 生成 32 位 UUID
		 * 
		 * @return
		 */
		public static String generateUUID(){
			
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
		
		/**
		 * 判断字符串是否为空
		 * 
		 * @param text
		 * @return
		 */
		public static boolean isEmpty(String text){
			
			if(text == null){
				return true;
			}
			
			text = text.trim().toLowerCase().replaceAll("'|\"", "");
			
			return "".equals(text) || "null".equals(text);
		}
		
		/**
		 * 获取 getter 方法的属性名称
		 * 
		 * @param getterName
		 * @return
		 */
		public static String getGetterPropertyName(String getterName){
			
			String propertyName = getterName.replaceFirst("get", "");
			return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
		}		/**
		 * 反格式化变量名称
		 * <br/>
		 * 将形如 personName 格式化为 person_name
		 * 
		 * @param varName
		 * @return
		 */
		public static String unFormatVariableWithLower(String varName){
			
			StringBuilder words = new StringBuilder();
			for(int i = 0; i < varName.length(); i ++){
				
				char c = varName.charAt(i);
				if(Character.isUpperCase(c)){
					
					words.append("_").append(Character.toLowerCase(c));
				}else{
					
					words.append(c);
				}
			}
			
			return words.toString();
		}
		
		/**
		 * 格式化变量名称
		 * <br/>
		 * 将形如 person_name/PERSON_NAME 格式化为 personName
		 * 
		 * @param varName
		 * @return
		 */
		public static String formatVariableWithLower(String varName){
			
			if(varName != null){
				
				varName = varName.toLowerCase();
				
				String newName = "";
				for(String part : varName.split("_")){
					
					newName += part.substring(0, 1).toUpperCase() + part.substring(1);
				}
				
				newName = newName.substring(0, 1).toLowerCase() + newName.substring(1);
				
				return newName;
			}
			
			return null;
		}
		
		
		/**
		 * 判断字符串是否非空
		 * 
		 * @param text
		 * @return
		 */
		public static boolean isNotEmpty(String text){
			
			return !isEmpty(text);
		}
		
		/**
		 * 格式化 DML/DQL 语句
		 * 
		 * @param source
		 * @return
		 */
		public static String formatSql4DMLOrDQL(String source){
			String result = "" ;
			try {
				result = new DMLFormatter().format((source + "").trim().replaceAll("\n", ""));
			} catch (Exception e) {
				logger.error("格式化SQL语句失败！"+e.getMessage());
			}
			return result;
		}
		
		/**
		 * 格式化 DDL 语句
		 * 
		 * @param source
		 * @return
		 */
		public static String formatSql4DDL(String source){
			
			return new DDLFormatter().format((source + "").trim().replaceAll("\n", ""));
		}
		
		/**
		 * @describe 获取详细堆栈信息
		 * @param t
		 * @return
		 */
		public static String getStackTrace(Throwable t) {
		    StringWriter sw = new StringWriter();
		    PrintWriter pw = new PrintWriter(sw);
		    try {
		        t.printStackTrace(pw);
		        return sw.toString();
		    } finally {
		        pw.close();
		    }
		}
		
		
		/**
		 * 简化字符化异常信息，返回堆栈首行和包含有 kyee, quyiyuan 等信息
		 * 
		 * @param throwable
		 * @return
		 */
		public static String getSimplifyStackTrace(Throwable throwable) {
		
			try{
				
				StackTraceElement[] traces = throwable.getStackTrace();
				
				int size = traces.length;
				if (size == 0) {
					return null;
				}
				
				StringBuilder sb = new StringBuilder();
				
				//始终保留第一行
				sb.append(throwable.toString()).append("\n\tat ").append(traces[0]).append("\n");
				
				for (int index = 1; index < size; index++) {
				
					String trace = traces[index].toString();
					
					if (trace.startsWith("com.kyee") || trace.startsWith("com.cpinfo")) {
						sb.append("\tat ").append(trace).append("\n");
					}
				}
				
				return sb.toString();
			}catch(Exception e) {
				logger.error("获取堆栈异常信息失败！"+e.getMessage());
			}
			return "";
		}
		
		/**
		 * 获取当前线程上下文堆栈
		 * 
		 * @param startsWith 过滤条件（按照 startsWith 规则过滤）
		 * @return
		 */
		public static String getCurrentStackTrace(String[] startsWith){
			
			StringBuilder content = new StringBuilder();
			
			StackTraceElement[] eles = Thread.currentThread().getStackTrace();
			if(eles != null && eles.length > 0){
				
				for(StackTraceElement ele : eles){
					
					String line = ele + "";
					
					if(startsWith != null){
						for(String st : startsWith){
							if(line.startsWith(st)){
								content.append(line).append("\n");
								continue;
							}
						}
					}else{
						content.append(line).append("\n");
					}
				}
			}
			
			return content.toString();
		}
		
	}
	
	/**
	 * 文件工具类
	 */
	public static class FileUtil{
		
		/**
		 * 检测类路径下或 config 路径下的配置文件是否存在
		 * 
		 * @param fileName
		 * @return
		 */
		public static boolean isFileExistOnClasspathOrConfigDir(String fileName){
			
			return isClassPathFileExist(fileName) || isFileExist("config" + File.separator + fileName);
		}
		
		/**
		 * 加载类路径下或 config 路径下的配置文件
		 * <br/>
		 * 优先在类路径下查找，如果不存在，则在 config 路径下查找
		 * 
		 * @param fileName
		 * @return
		 * @throws Exception 
		 */
		public static Properties loadPropertiesOnClassPathOrConfigDir(String fileName){
			
			if(isClassPathFileExist(fileName)){
				return loadClassPathProperties(fileName);
			}
			
			String path = "config" + File.separator + fileName;
			if(isFileExist(path)){
				return loadProperties(path);
			}
			
			throw new FrameworkInternalSystemException(new SystemExceptionDesc("classpath 下以及 config 目录下均没有找到配置文件 " + fileName + "，请确认其是否存在"));
		}
		
		
		/**
		 * 加载指定的 Properties 文件并返回
		 * 
		 * @param filePath
		 * @return
		 * @throws Exception 
		 */
		public static Properties loadProperties(String filePath){
			
			InputStream is = null;
			try {
				
				Resource resource = new FileSystemResource(filePath);
				
				if(resource.exists()){
					
					is = resource.getInputStream();
					
					Properties cfg = new Properties();
					
					String lowerCaseFilePath= filePath.toLowerCase();
					if(lowerCaseFilePath.endsWith(".properties")){
						cfg.load(is);
					}else if(lowerCaseFilePath.endsWith(".xml")){
						cfg.loadFromXML(is);
					}
					
					return cfg;
				}
				
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("没有找到配置文件 " + filePath + "，请确认该文件是否存在"));
			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("加载 " + filePath + " 时出错，请确认文件是否存在或者是否为标准的 properties 或 xml 文件", e));
			} finally {
				
				try {
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
					
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e2));
				}
			}
		}

		
		 /* 判断类路径下的文件是否存在
		 * 
		 * @param fileName
		 * @return
		 */
		public static boolean isClassPathFileExist(String fileName){
			
			return new ClassPathResource(fileName).exists();
		}
		
		
		/**
		 * 加载类路径下的 Properties 文件
		 * 
		 * @param fileName
		 * @return
		 * @throws Exception 
		 */
		public static Properties loadClassPathProperties(String fileName){
						
			Resource resource = new ClassPathResource(fileName);
			
			InputStream is = null;
			
			try {
				is = resource.getInputStream();
				Properties properties = new Properties();
				
				String lowerCaseFileName = fileName.toLowerCase();
				if(lowerCaseFileName.endsWith(".properties")){
					properties.load(is);
				}else if(lowerCaseFileName.endsWith(".xml")){
					properties.loadFromXML(is);
				}
				
				return properties;
			} catch (IOException e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("加载类路径下的 " + fileName + " 时出错，请确认文件是否存在或者是否为标准的 properties 或 xml 文件", e));
			} finally {
				
				try {
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
					
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e2));
				}
			}
		}
		
		/**
		 * 判断指定路径下的文件是否存在
		 * 
		 * @param filePath
		 * @return
		 */
		public static boolean isFileExist(String filePath){
			
			return new FileSystemResource(filePath).exists();
		}

		/**
		 * 获取类路径下或 config 路径下的文件输入流
		 * <br/>
		 * 优先在类路径下查找，如果不存在，则在 config 路径下查找
		 *
		 * @param fileName
		 * @return
		 */
		public static InputStream loadResourceAsStreamOnClassPathOrConfigDir(String fileName){

			if(isClassPathFileExist(fileName)){
				return loadClassPathResourceAsStream(fileName);
			}

			String path = "config" + File.separator + fileName;
			if(isFileExist(path)){
				try {
					return new FileSystemResource(path).getInputStream();
				} catch (Exception e) {
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
				}
			}

			throw new FrameworkInternalSystemException(new SystemExceptionDesc("classpath 下以及 config 目录下均没有找到配置文件 " + fileName + "，请确认其是否存在"));
		}

		/**
		 * 加载类路径下的文件，并以流的方式返回
		 *
		 * @param fileName
		 * @return
		 */
		public static InputStream loadClassPathResourceAsStream(String fileName){

			Resource resource = new ClassPathResource(fileName);

			if(!resource.exists()){
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("加载类路径下的文件 " + fileName + " 时出错，因为不存在该文件"));
			}

			try {
				return resource.getInputStream();
			} catch (IOException e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}

		/**
		 * 获取类路径下或 config 路径下的文件，并以文本的方式返回
		 * <br/>
		 * 优先在类路径下查找，如果不存在，则在 config 路径下查找
		 *
		 * @param fileName
		 * @return
		 */
		public static String loadResourceAsTextOnClassPathOrConfigDir(String fileName){

			return FileUtil.read(loadResourceAsStreamOnClassPathOrConfigDir(fileName));
		}

		/**
		 * 读取流，并以文本返回
		 *
		 * @param is
		 * @return
		 */
		public static String read(InputStream is){

			StringBuilder content = new StringBuilder();

			try {

				byte[] tmp = new byte[512];
				int len;

				while((len = is.read(tmp)) != -1){

					content.append(new String(tmp, 0, len));
				}

				is.close();

			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			} finally {

				try {

					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e2));
				}
			}

			return content.toString();
		}


		/**
		 * 搜索策略
		 */
		public enum SEARCH_STRATEGY{

			START_WIRH,
			ENDS_WITH,
			INDEXOF
		}

		/**
		 * 搜索指定目录下的符合条件的文件
		 *
		 * @param basePath 搜索目录
		 * @param keywords 文件过滤
		 * @param searchStrategy 搜索策略
		 * @param ignoreCase 是否忽略大小写的文件名称匹配
		 * @return
		 */
		public static List<File> searchFiles(String basePath, final String keywords, final SEARCH_STRATEGY searchStrategy, final boolean ignoreCase){
			List<File>  resultFiles = new ArrayList<>();
			File baseDir = new File(basePath);
			if(baseDir != null && baseDir.isDirectory()){

				File[] files = baseDir.listFiles(new FileFilter(){

					public boolean accept(File file) {

						if (file.isDirectory()) {
							List<File> files1 = searchFiles(file.getAbsolutePath(), keywords, searchStrategy, ignoreCase);
							resultFiles.addAll(files1);
						}

						if(searchStrategy == SEARCH_STRATEGY.START_WIRH){
							if(ignoreCase){
								return file.getName().toUpperCase().startsWith(keywords.toUpperCase());
							}else{
								return file.getName().startsWith(keywords);
							}
						}else if(searchStrategy == SEARCH_STRATEGY.ENDS_WITH){
							if(ignoreCase){
								return file.getName().toUpperCase().endsWith(keywords.toUpperCase());
							}else{
								return file.getName().endsWith(keywords);
							}
						}else if(searchStrategy == SEARCH_STRATEGY.INDEXOF){
							if(ignoreCase){
								return file.getName().toUpperCase().indexOf(keywords.toUpperCase()) != -1;
							}else{
								return file.getName().indexOf(keywords) != -1;
							}
						}
						return false;
					}
				});
				resultFiles.addAll(Arrays.asList(files));
				return resultFiles;
			}

			throw new FrameworkInternalSystemException(new SystemExceptionDesc("目录不存在"));
		}

		/**
		 * 压缩文件
		 *
		 * @param files 指定待压缩的文件
		 * @param outputFile
		 */
		public static void packFiles(List<String> files, String outputFile) {

			packFiles(files, outputFile, null);
		}


		/**
		 * 压缩文件
		 *
		 * @param scanPath 指定待扫描的目录（注意：仅扫描第一层目录）
		 * @param outputFile
		 */
		public static void packFiles(String scanPath, String outputFile) {

			File dir = new File(scanPath);
			if(!dir.isDirectory()){

				throw new FrameworkInternalSystemException(new SystemExceptionDesc("不是目录"));
			}

			File[] files = dir.listFiles();
			pack(Arrays.asList(files), outputFile, null);
		}

		/**
		 * 压缩文件
		 *
		 * @param files 待压缩文件
		 * @param outputFile 输出文件
		 * @param nameMapping 文件名称映射
		 */
		public static void packFiles(List<String> files, String outputFile, Map<String, String> nameMapping) {

			List<File> _files = new ArrayList<File>();

			File f = null;
			for(String file : files){

				f = new File(file);
				if(f.exists()){
					_files.add(f);
				}else{
					throw new FrameworkInternalSystemException(new SystemExceptionDesc("找不到文件 " + f));
				}
			}

			pack(_files, outputFile, nameMapping);
		}

		/**
		 * 压缩文档
		 *
		 * @param files 待压缩文件
		 * @param outputFile 输出文件
		 * @param nameMapping 文件名称映射
		 */
		private static void pack(List<File> files, String outputFile, Map<String, String> nameMapping){

			OutputStream fileOutputStream = null;
			CheckedOutputStream cos = null;
			ZipOutputStream out = null;
			try {
				fileOutputStream = new FileOutputStream(outputFile);
				cos = new CheckedOutputStream(fileOutputStream, new CRC32());
				out = new ZipOutputStream(cos);
				for(File file : files){

					if(file.isDirectory()){
						continue;
					}

					//映射文件名称
					String name = file.getName();
					String nameWidthoutSuffix = name.substring(0, name.indexOf("."));
					if(nameMapping != null && nameMapping.size() > 0 && nameMapping.containsKey(nameWidthoutSuffix)){
						String suffix = name.substring(name.indexOf("."));
						name = nameMapping.get(nameWidthoutSuffix) + suffix;
					}
					ZipEntry entry = new ZipEntry(name);
					out.putNextEntry(entry);

					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					int len;
					byte tmp[] = new byte[1024];
					while ((len = bis.read(tmp)) != -1) {
						out.write(tmp, 0, len);
					}
					bis.close();
				}

			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			} finally {
				try {
					if(out != null){
						out.flush();
						out.close();
					}
					if(cos != null){
						cos.close();
					}
					if(fileOutputStream != null){
						fileOutputStream.close();
					}
				} catch (Exception e2) {
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e2));
				}
			}
		}
		
	}
	
	
	/**
	 * 加密工具
	 */
	public static class SecurityUtil{
		
		private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
		
		/**
		 * MD5 加密类型 
		 */
		public enum MD5_LENGTH{
			
			LENGTH_16(16),
			LENGTH_32(21);
			
			int length;
			MD5_LENGTH(int length){
				
				this.length = length;
			}
		}
		
		/**
		 * 编码为 base64
		 * 
		 * @param src
		 * @return
		 */
		public static String encodeForBase64(String src){
			
			try {
				return Base64.encodeBase64String(src.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}
		
		/**
		 * 解码为 base64
		 * 
		 * @param src
		 * @return
		 */
		public static String decodeForBase64(String src){
			
			try {
				return new String(Base64.decodeBase64(src), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}
		
		/**
		 * MD5 摘要算法（16 位）
		 * <br/>
		 * 注意：包含汉字等特殊字符时，请使用 String MD5(String source, MD5_LENGTH length, Charset charset) 方法，并设置对应编码
		 * 
		 * @return
		 */
		public static String MD5_16(String source) {  
			
			return MD5(source, MD5_LENGTH.LENGTH_16, null);
	    }
		
		/**
		 * MD5 摘要算法（32 位）
		 * <br/>
		 * 注意：包含汉字等特殊字符时，请使用 String MD5(String source, MD5_LENGTH length, Charset charset) 方法，并设置对应编码
		 * 
		 * @return
		 */
		public static String MD5_32(String source) {  
			
			return MD5(source, MD5_LENGTH.LENGTH_32, null);
	    }
		
		/**
		 * MD5 摘要算法
		 * <br/>
		 * 注意：包含汉字等特殊字符时，请使用 String MD5(String source, MD5_LENGTH length, Charset charset) 方法，并设置对应编码
		 *
		 * @param source
		 * @param length
		 * @return
		 */
		public static String MD5(String source, MD5_LENGTH length) {
		
			return MD5(source, length, null);
		}
		
		/**
		 * MD5 摘要算法
		 * 
		 * @param source
		 * @param length
		 * @param charset
		 * @return
		 */
		public static String MD5(String source, MD5_LENGTH length, Charset charset) {
			
	        String result = "";
	        try {
	        	
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(charset == null ? source.getBytes() : source.getBytes(charset));
	            byte b[] = md.digest();
	            int i;
	            StringBuffer buf = new StringBuffer("");
	            for (int offset = 0; offset < b.length; offset++) {
	                i = b[offset];
	                if (i < 0)
	                    i += 256;
	                if (i < 16)
	                    buf.append("0");
	                buf.append(Integer.toHexString(i));
	            }
	            result = buf.toString();
	            
	            return length == MD5_LENGTH.LENGTH_32 ? result : buf.toString().substring(8, 24);
	        } catch (NoSuchAlgorithmException e) {
	            throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
	        }
	    }
		
		/**
		 * 生成密钥
		 */
		public static String generateKey(){
			
			try {
				
				KeyGenerator kg = KeyGenerator.getInstance("AES");
				kg.init(128);
				SecretKey secretKey = kg.generateKey();
				
				return Base64.encodeBase64String(secretKey.getEncoded());
			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}
		
		/**
		 * 加密数据
		 * 
		 * @param content
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static String encrypt(String content, String key){
						
			try {
				
				Key keyObj = new SecretKeySpec(Base64.decodeBase64(key), "AES");                     
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, keyObj);
				
				return Base64.encodeBase64String(cipher.doFinal(content.getBytes()));
			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}
		
		/**
		 * 解密数据
		 * 
		 * @param content
		 * @param key
		 * @return
		 * @throws Exception
		 */
		public static String decrypt(String content, String key){
						
			try {
				
				Key keyObj = new SecretKeySpec(Base64.decodeBase64(key), "AES");
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, keyObj);
				
				return new String(cipher.doFinal(Base64.decodeBase64(content)));
			} catch (Exception e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
			}
		}
	}
	
	
	
	
	
	/**
	 * 数据源工具类
	 */
	public static class DataSourceUtil {
		
		/**
		 * 数据源类型
		 */
		public enum DATA_SOURCE_TYPE {
			
			/**
			 * C3P0 数据源
			 */
			C3P0("com.mchange.v2.c3p0.ComboPooledDataSource");
			
			private String className;
			
			private DATA_SOURCE_TYPE(String className){
				this.className = className;
			}
			
			public String getClassName() {
				return className;
			}
			
			/**
			 * 是否包含指定类名
			 * 
			 * @param className
			 * @return
			 */
			public static boolean containsByClassName(String className){
				
				for(DATA_SOURCE_TYPE type : values()){
					
					if(type.getClassName().equals(className)){
						return true;
					}
				}
				
				return false;
			}
			
			/**
			 * 是否包含指定名称
			 * 
			 * @param name
			 * @return
			 */
			public static boolean containsByName(String name){
				
				for(DATA_SOURCE_TYPE type : values()){
					
					if(type.toString().equals(name)){
						return true;
					}
				}
				
				return false;
			}
		}
		
		/**
		 * 安装数据源
		 * 
		 * @param beanName
		 * @param properties
		 * @param dataSourceType
		 * @param context
		 */
		public static DataSource regist(String beanName, Map<String, Object> properties, DATA_SOURCE_TYPE dataSourceType, ApplicationContext context){
			
			if(context.containsBean(beanName)){
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("数据源 " + beanName + " 已存在，不能重复注册"));
			}
			
			DefaultListableBeanFactory factory = (DefaultListableBeanFactory)context.getAutowireCapableBeanFactory();
			
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(dataSourceType.getClassName());
			
			if(properties != null && properties.size() > 0){
				
				for(Entry<String, Object> property : properties.entrySet()){
					
					String name = property.getKey();
					Object value = property.getValue();
					
					if(name != null){
						builder.addPropertyValue(name, value);
					}
				}
			}
			
			factory.registerBeanDefinition(beanName, builder.getBeanDefinition());
			
			return context.getBean(beanName, DataSource.class);
		}
	}
	
	
	
	
	
	/**
	 * Properties 工具类
	 */
	public static class PropertiesUtil{
		
		
		/**
		 * 加载类路径下或 config 路径下的配置文件
		 * <br/>
		 * 优先在类路径下查找，如果不存在，则在 config 路径下查找
		 * 
		 * @param fileName
		 * @return
		 * @throws Exception 
		 */
		public static Properties loadPropertiesOnClassPathOrConfigDir(String fileName){
			
			return FileUtil.loadPropertiesOnClassPathOrConfigDir(fileName);
		}
		
		
		/**
		 * 加载类路径下的 Properties 文件
		 * 
		 * @param fileName
		 * @return
		 */
		public static Properties loadClassPathProperties(String fileName){
			
			return FileUtil.loadClassPathProperties(fileName);
		}

		
		
		/**
		 * 加载指定的 Properties 文件并返回
		 * 
		 * @param filePath
		 * @return
		 * @throws Exception 
		 */
		public static Properties loadProperties(String filePath) throws Exception{
			
			InputStream is = null;
			try {
				
				Resource resource = new FileSystemResource(filePath);
				
				if(resource.exists()){
					
					is = resource.getInputStream();
					
					Properties cfg = new Properties();
					
					String lowerCaseFilePath= filePath.toLowerCase();
					if(lowerCaseFilePath.endsWith(".properties")){
						cfg.load(is);
					}else if(lowerCaseFilePath.endsWith(".xml")){
						cfg.loadFromXML(is);
					}
					
					return cfg;
				}
				
				throw new Exception("没有找到配置文件 " + filePath + "，请确认该文件是否存在");
			} catch (Exception e) {
				throw new Exception("加载 " + filePath + " 时出错，请确认文件是否存在或者是否为标准的 properties 或 xml 文件", e);
			} finally {
				
				try {
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
					
					throw e2;
				}
			}
		}
		
		/**
		 * 过滤属性
		 * 
		 * @param prefix 要返回的属性名称前缀
		 * @param removePrefix 是否需要移除该属性前缀
		 * @param properties
		 * @return
		 */
		public static Map<String, Object> filter(String prefix, boolean removePrefix, Properties properties){
			
			return filter(prefix, null, removePrefix, properties);
		}
		
		/**
		 * 过滤属性
		 * 
		 * @param prefix 要返回的属性名称前缀
		 * @param skipPattern 跳过模式，正则表达式（注意：此表达式是基于移除 prefix 之后的字符串进行的）；例如："name|age"，即会跳过 name, age 的属性
		 * @param removePrefix 是否需要移除该属性前缀
		 * @param properties Properties 对象
		 * @return
		 */
		public static Map<String, Object> filter(String prefix, String skipPattern, boolean removePrefix, Properties properties){
			
			Map<String, Object> result = new HashMap<String, Object>();
			
			if(properties != null){
				
				for(String key : properties.stringPropertyNames()){
					
					//仅加载当前数据源的配置
					if(key.startsWith(prefix)){
						
						String simpleKey = key;
						
						//移除前缀
						if(removePrefix){
							simpleKey = simpleKey.replaceFirst(prefix, "");
						}
						
						//跳过模式
						if(skipPattern == null || !key.replaceFirst(prefix, "").matches(skipPattern)){
							result.put(simpleKey, properties.getProperty(key));
						}
					}
				}
			}
			
			return result;
		}


		public static void saveClassPathProperties(String fileName, Properties properties) {

			Resource resource = new ClassPathResource(fileName);

			OutputStream os = null;
			try {

				os = new FileOutputStream(resource.getFile());
				properties.store(os, null);

			} catch (IOException e) {
				throw new FrameworkInternalSystemException(new SystemExceptionDesc("加载类路径下的 " + fileName + " 时出错，请确认文件是否存在或者是否为标准的 properties 或 xml 文件", e));
			}finally {

				try {
					os.close();
				} catch (IOException e) {
					throw new FrameworkInternalSystemException(new SystemExceptionDesc(e));
				}
			}
		}
	}

	}

