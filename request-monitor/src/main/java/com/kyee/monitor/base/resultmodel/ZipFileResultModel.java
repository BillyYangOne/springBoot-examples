package com.kyee.monitor.base.resultmodel;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.internal.framework.FrameworkInternalSystemException;
import com.kyee.monitor.base.common.util.CommonUtils;
import com.kyee.monitor.base.common.util.CommonUtils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ZipFileResultModel extends StreamBasedResultModel {
    private List<String> paths;

    private String scanPath;

    private String name;

    private String originalPath;

    public ZipFileResultModel() {
    }

    public ZipFileResultModel(String name) {
        this.name = name;
    }

    public ZipFileResultModel(String scanPath, String name) {
        this.scanPath = scanPath;
        this.name = name;
    }

    public ZipFileResultModel(List<String> paths, String name) {
        this.paths = paths;
        this.name = name;
    }

    @Override
    protected InputStream getInputStream() throws Exception {

        if(this.paths != null && this.scanPath != null ){
            throw new FrameworkInternalSystemException(new SystemExceptionDesc("scanPath 和 paths 只能设置一个"));
        }

        String outputPath = System.getProperty("java.io.tmpdir") + File.separator + "kyee_tmpdir";
        this.originalPath = outputPath + File.separator + System.currentTimeMillis() + ".zip";

        File dir = new File(outputPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        if(this.paths != null){

            CommonUtils.FileUtil.packFiles(paths, originalPath);
            return new FileInputStream(originalPath);
        }

        if(this.scanPath != null){

            FileUtil.packFiles(scanPath, originalPath);
            return new FileInputStream(originalPath);
        }

        return null;
    }

    /**
     * 添加新文件
     *
     * @param path
     * @return
     */
    public ZipFileResultModel put(String path){

        if(this.paths == null){
            paths = new ArrayList<String>();
        }

        paths.add(path);

        return this;
    }

    @Override
    protected String getOutputName() {
        return name;
    }

    @Override
    protected String getOriginalPath() {
        return originalPath;
    }

    @Override
    protected boolean isAutoDelete() {
        //下载后删除压缩文件
        return true;
    }

    @Override
    protected ContentType getResultContentType() {
        return ContentType.ZIP;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }
}
