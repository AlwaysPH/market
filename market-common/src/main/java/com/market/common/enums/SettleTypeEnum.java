package com.market.common.enums;

import com.market.common.utils.StringUtils;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-05 22:01
 */
public enum SettleTypeEnum {

    FULL("0", "足额结算"),

    PRESTORE("1", "差额预存");

    private String code;

    private String name;

    SettleTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SettleTypeEnum getEnumByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(SettleTypeEnum data : SettleTypeEnum.values()){
            if(StringUtils.equalsIgnoreCase(data.getCode(), code)){
                return data;
            }
        }
        return null;
    }
}
