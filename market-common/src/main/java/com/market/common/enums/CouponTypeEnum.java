package com.market.common.enums;

import com.market.common.utils.StringUtils;

/**
 * 优惠券类型
 * @author ph
 * @version 1.0
 * @date 2023-04-19 19:19
 */
public enum  CouponTypeEnum {

    FULL("0", "满减券"),

    SALE("1", "折扣券"),

    VOUCHER("2", "代金券");

    private String code;

    private String name;

    CouponTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for (CouponTypeEnum data : values()){
            if(StringUtils.equalsIgnoreCase(data.getCode(), code)){
                return data.getName();
            }
        }
        return null;
    }
}
