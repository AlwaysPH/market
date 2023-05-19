package com.market.common.enums;

/**
 *
 * @author ph
 * @version 1.0
 * @date 2023-04-18 14:50
 */
public enum AvailableStatusEnum {

    AVAILABLE("0", "可用"),

    NO_AVAILABLE("1", "不可用");

    private String code;

    private String name;

    AvailableStatusEnum(String code, String name) {
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
