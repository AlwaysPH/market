package com.market.coupon.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-17 14:27
 */
@Data
public class AppParams implements Serializable {
    private static final long serialVersionUID = -1037900837832755278L;

    /**
     * 优惠券id
     */
    @NotNull(message = "优惠券id不能为空")
    private String couponId;

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    private String activityId;

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private String userNo;

    /**
     * 用户手机号
     */
    @NotNull(message = "用户手机号不能为空")
    private String phoneNumber;

    /**
     * APP账号
     */
    @NotNull(message = "APP账号不能为空")
    private String appAccount;

    /**
     * 卡号
     */
    @NotNull(message = "卡号不能为空")
    private String cardNumber;

    /**
     * 设备号
     */
    @NotNull(message = "设备号不能为空")
    private String deviceNo;

    /**
     * 领取时间
     */
    private String receiveTime;

    /**
     *领取方式  1 自动发放  2 手动领取
     */
    private String sendType;
}
