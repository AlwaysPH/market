package com.market.common.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * 满减券使用门槛
 * @author ph
 * @version 1.0
 * @date 2023-04-19 16:48
 */
public enum RestrictionTypeEnum {

    NO("0", "无限制"),

    FIX("1", "固定满减"),

    LADDER("2", "阶梯满减");

    private String code;

    private String name;

    RestrictionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static RestrictionTypeEnum getEnumByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(RestrictionTypeEnum e : RestrictionTypeEnum.values()){
            if(StringUtils.equalsIgnoreCase(e.getCode(), code)){
                return e;
            }
        }
        return null;
    }
}
