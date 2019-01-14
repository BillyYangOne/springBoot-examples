package com.kyee.monitor.base.resultmodel;


import com.kyee.monitor.base.common.util.CommonUtils;

public abstract class JsonBasedResultModel extends TextBasedResultModel {

    @Override
    protected String getText() {

        String ret = null;
        Object obj = getObject();
        if(obj != null){

            if(obj instanceof String){
                ret = obj + "";
            }else{
                ret = CommonUtils.JsonUtil.object2Json(obj);
            }
        }

        int len = (ret + "").length();
        if (len > 1048576) {
            String retTmp = ret.substring(0, 1000);
            logger.error("【！！Warning！！】This response size is more than 1M, the size is "
                    + len + " bytes, response is : " + retTmp + "...");
        }

        return ret;
    }

    @Override
    protected ContentType getResultContentType() {
        return ContentType.JSON;
    }

    /**
     * 获取待写入的对象
     *
     * @return
     */
    protected abstract Object getObject();

}
