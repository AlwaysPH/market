package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-06 11:11
 */
public enum CouponUseStatusEnum {

    UN_EFFECT("0", "未生效"),

    UN_USED("1", "未使用"),

    USED("2", "已使用"),

    TIME_OUT("3", "已失效");

    private String code;

    private String name;

    CouponUseStatusEnum(String code, String name) {
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
