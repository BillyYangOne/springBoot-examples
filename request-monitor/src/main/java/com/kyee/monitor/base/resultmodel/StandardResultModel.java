package com.kyee.monitor.base.resultmodel;

import com.kyee.monitor.base.common.util.CommonUtils;
import com.kyee.monitor.base.common.util.CommonUtils.DateUtil.DATE_PATTERN;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class StandardResultModel extends JsonBasedResultModel{
    private Object data;

    public StandardResultModel() {
    }

    public StandardResultModel(Object data) {
        this.data = data;
    }

    @Override
    public Object getObject() {

        Map<String, Object> rs = new LinkedHashMap<String, Object>();
        rs.put("success", true);
        rs.put("time", CommonUtils.DateUtil.formatDate(new Date(), DATE_PATTERN.yyyy_MM_dd_HH_mm_ss));

        if(data != null){
            rs.put("data", data);
        }

        return rs;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
