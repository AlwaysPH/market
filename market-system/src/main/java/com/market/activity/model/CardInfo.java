package com.market.activity.model;

import com.market.common.annotation.Excel;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * 卡信息
 * @author ph
 * @version 1.0
 * @date 2023-05-03 19:24
 */
@Data
@Table(name = "T_ACTIVITY_SPECIAL_USER")
public class CardInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4681339799545519192L;

    /**
     * 主键id
     */
    private String id;

    /**
     *活动ID
     */
    private String activityId;

    /**
     * 卡属性编号
     */
    @Excel(name = "卡属性编号")
    private String cardNo;

    /**
     * 卡属性名称
     */
    @Excel(name = "卡属性名称")
    private String cardName;

    /**
     * APPuserID
     */
    @Excel(name = "APPuserID")
    private String appUserId;

    /**
     * APP账号
     */
    @Excel(name = "APP账号")
    private String appAccount;

    /**
     * 企业客户名称
     */
    @Excel(name = "企业客户名称")
    private String companyUserName;

    /**
     * 卡面名称
     */
    @Excel(name = "卡面名称")
    private String cardFaceName;

    /**
     *删除标识 0 未删除   1  删除
     */
    private String delFlag;

}
