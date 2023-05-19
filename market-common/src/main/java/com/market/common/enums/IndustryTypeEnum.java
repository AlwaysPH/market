package com.market.common.enums;

import com.market.common.utils.StringUtils;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-05 21:55
 */
public enum IndustryTypeEnum {

    MALL("0", "大型商场"),

    SUPERMARKET("1", "大型超市"),

    ENTERTAINMENT("2", "娱乐"),

    CINEMA("3", "影院"),

    SHOP("4", "连锁便利店");

    private String code;

    private String name;

    IndustryTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static IndustryTypeEnum getEnumByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        for(IndustryTypeEnum data : IndustryTypeEnum.values()){
            if(StringUtils.equalsIgnoreCase(data.getCode(), code)){
                return data;
            }
        }
        return null;
    }
}
