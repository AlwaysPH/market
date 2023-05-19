package com.market.common.enums;

/**
 * 券有效期类型
 * @author ph
 * @version 1.0
 * @date 2023-04-18 11:19
 */
public enum CouponEffectTypeEnum {

    FIX("0", "固定日期"),

    ACC("1", "累计日期");

    private String code;

    private String name;

    CouponEffectTypeEnum(String code, String name) {
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
