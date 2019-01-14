package com.kyee.monitor.core.monitor.impl.sqlmonitor.sql.dbmonitor;


import java.util.Arrays;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.util.CommonUtils;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;
import com.kyee.monitor.config.constant.GlobalConstantEnum;
import com.kyee.monitor.core.boot.impl.BootService;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.impl.ControllerMonitor;


public abstract class DBMonitorComponent{

    protected static Log logger = LogFactory.getLog(DBMonitorComponent.class);

    protected static boolean monitorOn = false;

    protected static final String[] INCLUDE_STACK_TRACE_PGK = new String[]{"com.cpinfo", "com.kyee"};

    // 如果开启，则只保存异常消息
    protected static String filterNormalRecord = "off";

    static {

        Properties properties = null;
        try {
            properties = CommonUtils.PropertiesUtil.loadClassPathProperties(GlobalConstantEnum.MINITOR_CONFIG_FILE_NAME.getValue());
        } catch (Exception e) {
        	logger.error("基础文件 "+ GlobalConstantEnum.MINITOR_CONFIG_FILE_NAME.getValue() +" 配置有误！");
        }

        if ("on".equalsIgnoreCase(properties.getProperty("runningSwitch"))) {
            monitorOn = true;
        }

        filterNormalRecord = properties.getProperty("options.monitor.recordException", "off");
    }


    private static boolean isMcAgentBooted() {
        // 高频方法，使用静态类方法以提高速度
        return BootService.isBooted();
    }


    protected void packageMessage(String sql, String method, DetailMonitorResult monitorResult, long spendTime, long startTime, Object[] sqlParams) {

        try {
            if (monitorOn && isMcAgentBooted()) {
                if (ignoreCurrentRecord(monitorResult)) {
                    return;
                }

                constructMonitorResult(sql, method, monitorResult, spendTime, startTime, sqlParams);

                ControllerMonitor.getDetailMonitorResult().addAll(Arrays.asList(monitorResult));

            }
        } catch (Exception e) {
            logger.error("发送SQL监控失败，异常信息：" + e.getMessage());
        }
    }

    private void constructMonitorResult(String sql, String method, DetailMonitorResult monitorResult, long spendTime, long startTime, Object[] sqlParams) {
        
        monitorResult.setGroupId(ControllerMonitor.getGroupId());
        monitorResult.setStepId(ControllerMonitor.getMethodStepId());
        monitorResult.setTargetType(MonitorTargetType.SQL);
        monitorResult.setMethodName(method);
        monitorResult.setMethodContent(sql);
        monitorResult.setParams(JSON.toJSONString(sqlParams));
        monitorResult.setSpendTime(spendTime);
        monitorResult.setRunTime(startTime);
        
    }

    
    private boolean ignoreCurrentRecord(DetailMonitorResult monitorResult) {
        if ("on".equalsIgnoreCase(filterNormalRecord) && monitorResult.getException() == null) {
            return true;
        }
        return false;
    }

}
