package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-03 19:21
 */
public enum ReceiveTypeEnum {


    ALL("0", "全部用户"),

    NEW_USER("1", "新用户"),

    NEW_ORDER("2", "首单"),

    SPECIFY_USER("3", "指定用户");

    private String code;

    private String name;

    ReceiveTypeEnum(String code, String name) {
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
