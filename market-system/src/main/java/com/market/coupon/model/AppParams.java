package com.market.coupon.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

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
    @NotBlank(message = "优惠券id不能为空")
    @NotEmpty(message = "优惠券id不能为空")
    private String couponId;

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    @NotBlank(message = "活动id不能为空")
    @NotEmpty(message = "活动id不能为空")
    private String activityId;

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    @NotBlank(message = "用户编号不能为空")
    @NotEmpty(message = "用户编号不能为空")
    private String userNo;

    /**
     * 用户手机号
     */
    @NotNull(message = "用户手机号不能为空")
    @NotBlank(message = "用户手机号不能为空")
    @NotEmpty(message = "用户手机号不能为空")
    private String phoneNumber;

    /**
     * APP账号
     */
    @NotNull(message = "APP账号不能为空")
    @NotBlank(message = "APP账号不能为空")
    @NotEmpty(message = "APP账号不能为空")
    private String appAccount;

    /**
     * 卡号
     */
    @NotNull(message = "卡号不能为空")
    @NotBlank(message = "卡号不能为空")
    @NotEmpty(message = "卡号不能为空")
    private String cardNumber;

    /**
     * 设备号
     */
    @NotNull(message = "设备号不能为空")
    @NotBlank(message = "设备号不能为空")
    @NotEmpty(message = "设备号不能为空")
    private String deviceNo;

    /**
     * 领取时间
     */
    private String receiveTime;

    /**
     *领取方式  1 自动发放  2 手动领取
     */
    private String sendType;

    /**
     * 用户类型  0  全部  1  新用户   2  首单
     */
    private Integer userType;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 订单原总金额
     */
    private BigDecimal orderAmount;

    /**
     * 用户领取优惠券表id
     */
    private String userCouponId;
}
