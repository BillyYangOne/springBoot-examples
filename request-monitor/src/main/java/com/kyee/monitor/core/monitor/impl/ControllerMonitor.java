package com.kyee.monitor.core.monitor.impl;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.exception.BaseException;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.common.exception.beans.BaseExceptionDesc;
import com.kyee.monitor.base.monitor.BaseMonitor;
import com.kyee.monitor.base.resultmodel.TextBasedResultModel;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.bean.impl.ControllerMonitorResult;
import org.apache.http.HttpHeaders;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component("controllerMonitor")
public class ControllerMonitor extends BaseMonitor {


	/**
	 * 用于存储 @RequestBody 下的内容文本缓存名称
	 */
	public static final String REQUEST_BODY_CONTENT_CACHE_NAME = "REQUEST_BODY_CONTENT_CACHE_NAME";


	@Override
	public Object doMonitor(ProceedingJoinPoint joinpoint) throws Throwable {
		
		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		
		Method method = signature.getMethod();
		
		HttpServletRequest request = getHttpServletRequest();

		String requestHeader = request.getHeader(HttpHeaders.CONTENT_TYPE);

		try {
			if(requestHeader != null && requestHeader.toLowerCase().startsWith("application/json")){

				Object[] args = joinpoint.getArgs();

				if(args.length > 0 ){

					Annotation[][] annotations = method.getParameterAnnotations();

					String requestParams = "";

					for (int i = 0; i < args.length; i++) {

						if (annotations.length > 0) {

							for (Annotation annotation : annotations[i]) {

								if (annotation.annotationType() == RequestBody.class) {
									requestParams = JSON.toJSONString(args[i]);
									break;
								}
							}
						}
					}
					request.setAttribute(REQUEST_BODY_CONTENT_CACHE_NAME, requestParams);
				}else{
					request.setAttribute(REQUEST_BODY_CONTENT_CACHE_NAME, "{}");
				}
			}
		} catch (Exception e) {
			logger.error("获取Content-Type为：application/json类型参数失败！ "+e.getMessage());
		}

		//获取分组ID
		getGroupId(null);

		long startTs = -1L;
		long spendTime = -1L;
		
		String exception = null;
		ExceptionType exceptionType = null;
		String exceptionName = null;
		Object result = null;
		Integer stepId = getStepId();
		String exceptionMsg = null;
		String resultCode = null;
		
		try {
			startTs = System.currentTimeMillis();
			result = joinpoint.proceed();
			spendTime = System.currentTimeMillis() - startTs;
		} catch (Exception e) {
			
			spendTime = System.currentTimeMillis() - startTs;
			exception = getExceptionStackTrace(e);
			exceptionType = getExceptionType(e);
			exceptionName = getExceptionName(e);
			
			if(e instanceof BaseException) {
				BaseExceptionDesc exceptionDesc = ((BaseException)e).getDesc(); 
				exceptionMsg = exceptionDesc.getMessage();
				resultCode = exceptionDesc.getCode();
			}
			throw e;
		} finally {
			
			ControllerMonitorResult item = new ControllerMonitorResult(
					getGroupId(MonitorTargetType.CONTROLLER),
					stepId,
					joinpoint.getTarget().getClass().getName(),
					MonitorTargetType.CONTROLLER,
					method + "",
					getHttpRequestParams(MonitorTargetType.CONTROLLER),
					exception,
					exceptionType,
					exceptionName,
					exceptionMsg,
					resultCode,
					spendTime,
					startTs,
					request.getRequestURL() + "",
					request.getLocalAddr(),
					request.getServerPort(),
					getResponse(result),
					getSessionParams(request.getSession(false)),
					request.getRemoteAddr());

			publishControllerMonitorEvent(item);

		}
		
        return result;
    
	}


	/**
	 * 响应字段记录开关
	 */
	private static String responseMonitorSwitch ;

	/**
	 * 记录长度，默认100
	 */
	private static Integer responseLength = 100;

	@PostConstruct
	private void init(){

		Properties agentConfigItem = getConfigurationService().getApplicationCfg();

		responseMonitorSwitch = agentConfigItem.getProperty("options.monitor.responseMonitorSwitch");

		if (responseMonitorSwitch != null){
			try{
				responseLength = Integer.valueOf(agentConfigItem.getProperty("options.monitor.responseLength"));
			} catch (Exception e) {
				logger.error("options.monitor.responseLength 未配置或配置错误，使用默认值100");
			}
		}
	}


	/**
	 * 获取响应字段
	 * @param result
	 * @return
	 */
	private String getResponse(Object result) {

		if (result == null) {
			return "";
		}

		if ("on".equalsIgnoreCase(responseMonitorSwitch) || "true".equalsIgnoreCase(responseMonitorSwitch)) {

			//不符合框架ResultModel规范的返回值转换为json string后直接记录
			if (!(result instanceof TextBasedResultModel)) {

				String response = null;
				try {
					response = com.alibaba.fastjson.JSON.toJSONString(result);
				} catch (Exception e) {
					logger.error("Response转换Json出错：", e);
					response = "Response转换Json出错";
				}

				if(response.length() > responseLength) {
					response = response.substring(0, responseLength);
				}
				return response;
			}

			//反射调用protect String getText()获取响应文本
			Method getText = null;
			String response = null;
			try {

				getText = TextBasedResultModel.class.getDeclaredMethod("getText");
				getText.setAccessible(true);
				response = (String)getText.invoke(result, new Object[]{});

			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				logger.error("获取Response的getText()方法时发生异常：", e);
				return "获取Response的getText()方法时发生异常";
			}

			if (response == null) {
				return "Response的getText()方法返回了空";
			}

			//截断
			if(response.length() > responseLength) {
				response = response.substring(0, responseLength);
			}

			return response;
		}

		return null;

	}


	/**
	 * 获取Session参数
	 * @param session
	 * @return
	 */
	private String getSessionParams(HttpSession session) {

		String result = null;

		if (session == null) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			Enumeration<String> attributes = session.getAttributeNames();
			Map<String, Object> sessionParamsMap = new HashMap<>(5);

			while (attributes.hasMoreElements()) {
				String attributeName = attributes.nextElement();
				sessionParamsMap.put(attributeName, session.getAttribute(attributeName));
			}
			result = JSON.toJSONString(sessionParamsMap);
		} catch (Exception e) {
			logger.error("获取Session参数失败！"+e.getMessage());
		}
		return result;
	}

	public static String getGroupId(){
		return getGroupId(MonitorTargetType.SQL);
	}

	public static Integer getMethodStepId(){
		return getStepId();
	}


}
