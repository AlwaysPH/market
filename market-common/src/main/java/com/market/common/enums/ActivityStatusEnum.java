package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-23 19:49
 */
public enum ActivityStatusEnum {

    NO_START("0", "未开始"),

    ING("1", "进行中"),

    FAILURE("2", "已失效"),

    STOP("3", "已停止");

    private String code;

    private String name;

    ActivityStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
