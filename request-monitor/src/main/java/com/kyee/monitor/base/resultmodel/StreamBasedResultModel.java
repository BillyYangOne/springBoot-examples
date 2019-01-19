package com.kyee.monitor.base.resultmodel;


import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.internal.framework.FrameworkInternalSystemException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class StreamBasedResultModel extends ResultModel{
    @Override
    protected void render(HttpServletResponse response) throws Exception {
        //注意将 getOutputName() 放置于 getInputStream() 方法之后，因为子类可能在 getInputStream() 包含了 outputname 的空值初始化
        InputStream is = getInputStream();
        String name = getOutputName();

        if(name == null || is == null){
            throw new FrameworkInternalSystemException(new SystemExceptionDesc("name 或 input stream 为空"));
        }

        String newName = new String(name.getBytes("GB2312"), "ISO_8859_1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");

        OutputStream os = response.getOutputStream();
        byte[] tmp = new byte[512];
        int len;
        while((len = is.read(tmp)) != -1){

            os.write(tmp, 0, len);
        }
        is.close();
        os.flush();

        //执行自动删除
        if(isAutoDelete()){
            new File(getOriginalPath()).delete();
        }
    }

    @Override
    protected ContentType getResultContentType() {
        return null;
    }

    /**
     * 获取输入流
     *
     * @return
     * @throws Exception
     */
    protected abstract InputStream getInputStream() throws Exception;

    /**
     * 获取原始文件路径
     * <br/>
     * 该路径仅用于下载后删除原始文件，因此仅当 isAutoDelete() 方法返回 true 时，此参数才有意义
     *
     * @return
     */
    protected abstract String getOriginalPath();

    /**
     * 获取输出文件名称
     *
     * @return
     */
    protected abstract String getOutputName();

    /**
     * 是否下载后自动删除
     *
     * @return
     */
    protected abstract boolean isAutoDelete();
}
