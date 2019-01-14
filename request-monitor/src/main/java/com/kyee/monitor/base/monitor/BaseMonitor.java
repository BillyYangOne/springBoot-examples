package com.kyee.monitor.base.monitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.exception.BaseException;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.common.exception.impl.SystemException;
import com.kyee.monitor.base.common.util.CommonUtils;
import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.base.event.MonitorResultEvent;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;
import com.kyee.monitor.config.constant.GlobalConstantEnum;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.bean.impl.ControllerMonitorResult;
import com.kyee.monitor.core.monitor.impl.ControllerMonitor;
import com.kyee.monitor.core.monitor.vo.RequestMonitorResult;

/**
 * 基础监控器
 */
public abstract class BaseMonitor extends BaseComponent implements IMonitor {

	protected static  Log logger =LogFactory.getLog(BaseMonitor.class);

	private final static String[] INCLUDE_STACK_TRACE_PGK = new String[]{"com.quyiyuan", "com.kyee","com.cpinfo"};

	//保存分组的 id
	protected static ThreadLocal<String> groupIdCache = new ThreadLocal<String>();


	protected static ThreadLocal<String> requestParamsCache = new ThreadLocal<String>();

	//保存执行步骤编号
	protected static ThreadLocal<Integer> stepIdCache = new ThreadLocal<Integer>();

	//项目配置
	protected static Properties applicationCfg;

	//提供可配置的执行步骤过滤范围
	private static Integer maxStepId;

	// 异常开关，如果打开则只保存异常记录
	protected static String  filterNormalRecord = "off";

	//保存监控结果集
	protected static ThreadLocal<RequestMonitorResult> monitorResultThreadLocal = new ThreadLocal<>();

	static {
		
		String minitorConfigFile = GlobalConstantEnum.MINITOR_CONFIG_FILE_NAME.getValue();

		if (CommonUtils.FileUtil.isFileExistOnClasspathOrConfigDir(minitorConfigFile)) {

			applicationCfg = CommonUtils.PropertiesUtil.loadPropertiesOnClassPathOrConfigDir(minitorConfigFile);

			String stepIdCfg = applicationCfg.getProperty("system.monitor.request.count", "50");

			if (!CommonUtils.TextUtil.isEmpty(stepIdCfg)) {

				if (Integer.valueOf(stepIdCfg) > 150) {
					maxStepId = 150;
				} else {
					maxStepId = Integer.valueOf(stepIdCfg);
				}
			}
			logger.info("监控消息执行步骤记录id初始化完成，限制记录最大stepId为：" + maxStepId);
			
			filterNormalRecord = applicationCfg.getProperty("options.monitor.recordException","off");

			logger.info("监控消息记录异常开关初始化完成，filterNormalRecord="+filterNormalRecord);
		}else {
			logger.warn("监控配置文件" + minitorConfigFile + "不存在，请检查配置！");
		}

	}


	/**
	 * 监控事件发布
	 */
	protected void publishControllerMonitorEvent(ControllerMonitorResult item){

		setThreadControllerMonitorResult(item);
		
		//将controller信息也放入detail中
		getDetailMonitorResult().addAll(Arrays.asList(new DetailMonitorResult(
				item.getGroupId(), 
				item.getStepId(), 
				item.getTargetType(), 
				item.getMethodName(),
				item.getSourceUri(), 
				item.getRequestParams(), 
				item.getException() !=null ? true: false,
				item.getException(), 
				item.getExceptionType(), 
				item.getExceptionName(),
				item.getSpendTime(),
				item.getRunTime())));
		
		publishMonitorEvent(monitorResultThreadLocal.get());

		monitorResultThreadLocal.remove();
		// 移除执行步骤编号
		stepIdCache.remove();
		groupIdCache.remove();
	}

	/**
	 * 获取执行步骤编号
	 *
	 * @return
	 */
	protected static Integer getStepId(){

		if(stepIdCache.get() == null){
			stepIdCache.set(0);
		}

		Integer stepId = stepIdCache.get() + 1;
		stepIdCache.set(stepId);

		return stepId;
	}

	/**
	 * 发布监控事件
	 */
	private void publishMonitorEvent(RequestMonitorResult item){

		if ("on".equalsIgnoreCase(filterNormalRecord)) {
			if (item.getControllerMonitorResult().getException() == null) {
                 return;
			}
		}
		publishEvent(new MonitorResultEvent(item));
	}

	/**
	 * 获取分组 id
	 */
	protected static String getGroupId(MonitorTargetType targetType){

		if(groupIdCache.get() == null){
			groupIdCache.set(CommonUtils.TextUtil.generateUUID());
		}

		String id = groupIdCache.get();

		//控制层时移除分组 id（控制层为出口）
		if(targetType == MonitorTargetType.CONTROLLER){
			groupIdCache.remove();
		}

		return id;
	}

	/**
	 * @describe 获取请求参数
	 *
	 * @return
	 */
	protected static String getHttpRequestParams(MonitorTargetType targetType){

		String currRequestParams = requestParamsCache.get();
		if(currRequestParams == null){

			HttpServletRequest request = getHttpServletRequest();
			if(request != null){

				//如果为 @RequestBody 形式的请求，则直接读取 ControllerMonitor 预先存储的 request attr 值
				String requestHeader = request.getHeader(HttpHeaders.CONTENT_TYPE);
				if(requestHeader != null && requestHeader.toLowerCase().startsWith("application/json")){

					String postData = request.getAttribute(ControllerMonitor.REQUEST_BODY_CONTENT_CACHE_NAME) + "";
					request.removeAttribute(ControllerMonitor.REQUEST_BODY_CONTENT_CACHE_NAME);

					currRequestParams = postData;
					requestParamsCache.set(postData);
				}else{

					@SuppressWarnings("unchecked")
					Map<String, String[]> params = request.getParameterMap();

					String paramsStr = "{}";
					try {
						if(params != null && params.size() > 0){

							Map<String, String> targetParams = new HashMap<String, String>();
							for(Entry<String, String[]> entry : params.entrySet()){

								String[] value = entry.getValue();
								if(value != null){
									targetParams.put(entry.getKey(), value.length == 1 ? value[0] : Arrays.toString(value));
								}
							}

							paramsStr = JSON.toJSONString(targetParams);
						}
					} catch (Exception e) {
						logger.error("获取请求参数异常！"+e.getMessage());
					}

					currRequestParams = paramsStr;
					requestParamsCache.set(paramsStr);
				}
			}
		}

		//控制层时移除请求参数缓存（控制层为出口）
		if(targetType == MonitorTargetType.CONTROLLER){
			requestParamsCache.remove();
		}

		return currRequestParams;
	}

	/**
	 * 获取 HttpServletRequest
	 *
	 * @return
	 */
	protected static HttpServletRequest getHttpServletRequest() {

		RequestAttributes ra = RequestContextHolder.getRequestAttributes();

		if(ra != null){
			return ((ServletRequestAttributes) ra).getRequest();
		}

		return null;
	}

	/**
	 * 获取目标 uri
	 *
	 * @return
	 */
	protected static String getSourceUri(){

		HttpServletRequest req = getHttpServletRequest();

		if(req != null){
			return req.getRequestURL() + "";
		}

		return null;
	}

	/**
	 * 获取异常类型
	 *
	 * @param e
	 * @return
	 */
	protected ExceptionType getExceptionType(Exception e) {

		if(e instanceof BaseException){
			BaseException be = (BaseException)e;
			return be.defExceptionType();
		}

		if (e.getClass().getSimpleName().equals("BusinessException")) {
			return ExceptionType.BUSINESS_EXCEPTION;
		}

		//如果不属于 BaseException，则为 java 原始异常，一并归入 SYSTEM_EXCEPTION 处理
		return ExceptionType.SYSTEM_EXCEPTION;
	}

	/**
	 * 获取异常名称
	 *
	 * @param e
	 * @return
	 */
	protected String getExceptionName(Exception e){

		if(e instanceof BaseException){
			BaseException be = (BaseException)e;
			String shortName = be.defShortName();

			if(CommonUtils.TextUtil.isNotEmpty(shortName)){

				return shortName + "（" + e.getClass().getSimpleName() + "）";
			}
		}

		if (e.getClass().getSimpleName().equals("BusinessException")) {
			return "业务异常（BusinessException）";
		}

		return "系统异常（" + e.getClass().getSimpleName() + "）";
	}

	/**
	 * 获取异常堆栈信息
	 *
	 * @param e
	 * @return
	 * @throws Throwable
	 */
	protected String getExceptionStackTrace(Exception e) throws Throwable{

		//如果是系统类异常，则返回真实堆栈信息
		if(e instanceof SystemException){

			SystemException se = (SystemException)e;

			if(se != null && se.getDesc().getThrowable() != null){
				return CommonUtils.TextUtil.getSimplifyStackTrace(se.getDesc().getThrowable());
			}
		}

		return CommonUtils.TextUtil.getSimplifyStackTrace(e);
	}

	/**
	 * 获取当前线程的堆栈信息
	 *
	 * @return
	 */
	protected String getCurrentStackTrace(){

		return CommonUtils.TextUtil.getCurrentStackTrace(INCLUDE_STACK_TRACE_PGK);
	}


	public static void setThreadControllerMonitorResult(ControllerMonitorResult monitorResult){

		if (monitorResultThreadLocal.get() == null) {
			monitorResultThreadLocal.set(new RequestMonitorResult());
		}
		monitorResultThreadLocal.get().setControllerMonitorResult(monitorResult);
	}

	public static List<DetailMonitorResult> getDetailMonitorResult() {
		
		if (monitorResultThreadLocal.get() == null) {
			monitorResultThreadLocal.set(new RequestMonitorResult());
		}
		return monitorResultThreadLocal.get().getDetailMonitorResult();
	}

}
