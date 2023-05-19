package com.market.common.enums;

/**
 * 折扣上限标识
 * @author ph
 * @version 1.0
 * @date 2023-04-19 21:04
 */
public enum UpperLimitTypeEnum {

    NO("0", "否"),

    YES("1", "是");

    private String code;

    private String name;

    UpperLimitTypeEnum(String code, String name) {
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
