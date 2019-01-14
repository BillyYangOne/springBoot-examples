package com.kyee.monitor.core.monitor.impl.sqlmonitor.sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;

import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.common.util.CommonUtils.JsonUtil;
import com.kyee.monitor.base.common.util.CommonUtils.TextUtil;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.impl.sqlmonitor.BaseSqlMonitor;


/**
 * jdbc sql 监控器
 */
@Aspect
@Component("jdbcTemplateAndUtilsSqlMonitor")
public class JdbcTemplateAndUtilsSqlMonitor extends BaseSqlMonitor {
	
	@Override
	public Object doMonitor(ProceedingJoinPoint joinpoint) throws Throwable {
		 
		long startTs = -1L;
		long spendTime = -1L;
		
		Exception exceptionObj = null;
		String exception = null;
		ExceptionType exceptionType = null;
		String exceptionName = null;
		Object result = null;
		Integer stepId = getStepId();
		
		try {
			
			startTs = System.currentTimeMillis();
			result = joinpoint.proceed();
			spendTime = System.currentTimeMillis() - startTs;
		} catch (Exception e) {
			spendTime = System.currentTimeMillis() - startTs;
			exception = getExceptionStackTrace(e);
			exceptionType = getExceptionType(e);
			exceptionName = getExceptionName(e);
			exceptionObj = e;
			
			throw e;
		} finally {
			
			//由于框架已统一对 EmptyResultDataAccessException 进行了屏蔽，因此亦不需要抛出此类异常监控
			if(!(exceptionObj instanceof EmptyResultDataAccessException)){
				
				Object[] args = joinpoint.getArgs();
				if(args.length > 0){
					
					//按照 NamedParameterJdbcTemplate 以及 BaseDBUtils 的方法规范，第一个参数为 sql
					String sql = TextUtil.formatSql4DMLOrDQL(args[0] + "");
					String sqlParams = buildSqlParams(args);
					
					getDetailMonitorResult().addAll(Arrays.asList(new DetailMonitorResult(getGroupId(MonitorTargetType.SQL),
							stepId,
							MonitorTargetType.SQL,
							joinpoint.getSignature().getName(),
							sql,
							sqlParams,
							exception != null? true:false,
							exception,
							exceptionType,
							exceptionName,
							spendTime,
							startTs)));
					
				}
			}
		}
		
        return result;
	}
	
	/**
	 * 构建 sql 参数
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String buildSqlParams(Object[] args){

		String result = "";
		try {
			Map<String, Object> sqlParams = new HashMap<String, Object>();

			if(args != null && args.length > 1){

				for(int i = 1; i < args.length; i ++){

					Object param = args[i];

					//处理 BeanPropertySqlParameterSource 参数的情况，因此内置的 save,update,delete 均采用此类参数
					if(param instanceof BeanPropertySqlParameterSource){

						BeanPropertySqlParameterSource bpsps = (BeanPropertySqlParameterSource)param;

						String[] names = bpsps.getReadablePropertyNames();
						if(names != null && names.length > 0){

							for(String name : names){

								//跳过内置的属性
								if(!name.matches("class|dataHandler|metadata|sqlBuilder")){
									sqlParams.put(name, bpsps.getValue(name));
								}
							}
						}

					}else if(param instanceof Map){
						sqlParams.putAll((Map<String, Object>)param);
					}
				}
			}
			result = JsonUtil.object2Json(sqlParams);
		} catch (IllegalArgumentException e) {
			logger.error("构建sql参数异常！ "+e.getMessage());
		}

		return result;
	}
}
