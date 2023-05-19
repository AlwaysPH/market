package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-04 16:22
 */
public enum OperationTypeEnum {

    INSERT("0", "新增"),

    UPDATE("1", "修改");

    private String code;

    private String name;

    OperationTypeEnum(String code, String name) {
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
