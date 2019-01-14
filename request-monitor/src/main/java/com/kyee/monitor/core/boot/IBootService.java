package com.kyee.monitor.core.boot;

/**
 * 监控启动服务接口
 */
public interface IBootService {

    /**
     * @ 启动
     */
    void boot();

    /**
     * @ 暂停
     */
    void pause();

    /**
     * @ 恢复
     */
    void resume();

}
