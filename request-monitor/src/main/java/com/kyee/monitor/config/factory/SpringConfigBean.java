package com.kyee.monitor.config.factory;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kyee.monitor.core.monitor.impl.sqlmonitor.sql.MybatisSqlMonitor;

/**
 *@describe 配置类  （mybatis拦截器）
 */
@Configuration
public class SpringConfigBean {

    @Bean(name = "myBatisInterceptors")
    public Interceptor[] mybatisInterceptors(@Qualifier("mybatisSqlInterceptor")MybatisSqlMonitor mybatisSqlMonitor) {

        return new Interceptor[]{mybatisSqlMonitor};
    }

}
