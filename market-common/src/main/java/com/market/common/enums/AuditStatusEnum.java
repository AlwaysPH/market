package com.market.common.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-16 12:51
 */
public enum AuditStatusEnum {

    REVIEWED("0", "待审核"),

    PASS("1", "审核通过"),

    REJECT("2", "审核拒绝"),

    TO_BE_SUBMIT("3", "待提交");

    private String code;

    private String name;

    AuditStatusEnum(String code, String name) {
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
