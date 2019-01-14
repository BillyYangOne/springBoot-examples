package com.kyee.monitor.config.constant;

/**
 * @describe 监控全局常量
 */
public enum GlobalConstantEnum {

    MINITOR_CONFIG_FILE_NAME("monitor-config.properties");

    private String value;

    GlobalConstantEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
