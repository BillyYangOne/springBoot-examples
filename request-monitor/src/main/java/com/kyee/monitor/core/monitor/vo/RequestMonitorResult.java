package com.kyee.monitor.core.monitor.vo;

import java.util.ArrayList;
import java.util.List;

import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.impl.ControllerMonitorResult;

/**
 * @describe 访问记录消息集合
 *
 */
public class RequestMonitorResult {

    private ControllerMonitorResult controllerMonitorResult;
    
    private List<DetailMonitorResult> detailMonitorResult;

    public RequestMonitorResult() {

        controllerMonitorResult = new ControllerMonitorResult();
        
        detailMonitorResult = new ArrayList<>();
    }

    public ControllerMonitorResult getControllerMonitorResult() {
        return controllerMonitorResult;
    }

    public void setControllerMonitorResult(ControllerMonitorResult controllerMonitorResult) {
        this.controllerMonitorResult = controllerMonitorResult;
    }
    
    public List<DetailMonitorResult> getDetailMonitorResult(){
    	return detailMonitorResult;
    }
    
    public void setDetailMonitorResult(List<DetailMonitorResult> detailMonitorResult) {
    	this.detailMonitorResult = detailMonitorResult;
    }
}
