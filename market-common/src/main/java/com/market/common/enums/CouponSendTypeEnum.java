package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-06 11:11
 */
public enum CouponSendTypeEnum {

    AUTOMATIC("1", "自动发放"),

    MANUAL("2", "手动领取");

    private String code;

    private String name;

    CouponSendTypeEnum(String code, String name) {
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
