package com.kyee.monitor.core.monitor.impl.sqlmonitor.sql;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.impl.sqlmonitor.BaseSqlMonitor;

@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Component(value = "mybatisSqlInterceptor")
public class MybatisSqlMonitor extends BaseSqlMonitor implements Interceptor{

    @Override
    public Object doMonitor(ProceedingJoinPoint joinpoint){
        return null;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTs = -1L;
        long spendTime = -1L;

        String exception = null;
        ExceptionType exceptionType = null;
        String exceptionName = null;
        Integer stepId = getStepId();

        Object result;
        try {

            startTs = System.currentTimeMillis();
            result = invocation.proceed();
            spendTime = System.currentTimeMillis() - startTs;
        } catch (Exception e) {
            spendTime = System.currentTimeMillis() - startTs;
            exception = getExceptionStackTrace(e);
            exceptionType = getExceptionType(e);
            exceptionName = getExceptionName(e);
            throw e;
        } finally {

            String sql = null;
            String sqlParams = null;
            try {
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object parameter = null;
                if (invocation.getArgs().length > 1) {
                    parameter = invocation.getArgs()[1];
                }

//                String sqlId = mappedStatement.getId();
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                Configuration configuration = mappedStatement.getConfiguration();
                try {
                    Map<String, String> sqlAndParams = getSql(configuration, boundSql);
                    sql = sqlAndParams.get("sql");
                    sqlParams = sqlAndParams.get("params");
                } catch (Exception e) {
                    logger.info("获取mybatis的sql失败！" +e.getMessage());
                }

            } catch (Exception e) {
                logger.info("mybatis中SQL监控失败！" +e.getMessage());
            }

            getDetailMonitorResult().addAll(Arrays.asList(new DetailMonitorResult(getGroupId(MonitorTargetType.SQL),
					stepId,
					MonitorTargetType.SQL,
					invocation.getMethod().getName(),
					sql,
					sqlParams,
					exception != null? true:false,
					exception,
					exceptionType,
					exceptionName,
					spendTime,
					startTs)));

        }

        return result;
    }

    private Map<String,String> getSql(Configuration configuration, BoundSql boundSql) {

        Map<String,String> sqlAndParams = new HashMap<>();
        List<String> params = new ArrayList<>();

        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql
                .getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration
                    .getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                params.add(getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration
                        .newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    params.add(getSqlParam(boundSql, sql, metaObject, propertyName));
                }
            }
        }
        sqlAndParams.put("sql",sql);
        sqlAndParams.put("params",JSON.toJSONString(params));
        return sqlAndParams;
    }

    private static String getSqlParam(BoundSql boundSql, String sqlb, MetaObject metaObject, String propertyName) {
        if (metaObject.hasGetter(propertyName)) {
            Object obj = metaObject.getValue(propertyName);
            return getParameterValue(obj);
        } else if (boundSql.hasAdditionalParameter(propertyName)) {
            Object obj = boundSql
                    .getAdditionalParameter(propertyName);
            return  getParameterValue(obj);
        }else{//参数中没有传递该值,提醒该字段异常
            return "缺失";
        }
    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = obj.toString();
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(
                    DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = formatter.format(new Date());
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object o) {

        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

        //empty
    }
}
