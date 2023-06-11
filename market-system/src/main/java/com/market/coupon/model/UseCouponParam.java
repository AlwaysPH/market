package com.market.coupon.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-22 09:50
 */
@Data
public class UseCouponParam implements Serializable {
    private static final long serialVersionUID = 3746887430717353562L;

    /**
     * 用户关联优惠券id
     */
    @NotBlank(message = "用户关联优惠券id不能为空")
    private String userCouponId;

    /**
     * 优惠券id
     */
    @NotBlank(message = "优惠券id不能为空")
    private String couponId;

    /**
     * 用户手机号
     */
    @NotBlank(message = "用户手机号不能为空")
    private String phoneNumber;

    /**
     * 交易金额
     */
    @NotNull(message = "交易金额不能为空")
    private BigDecimal orderAmount;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 优惠金额
     */
    @NotNull(message = "优惠金额不能为空")
    private BigDecimal disAmount;

    /**
     * 标记是否是新用户  0 否  1 是
     */
    @NotNull(message = "新用户标识不能为空")
    private Integer userType;

    /**
     * 交易时间
     */
    @NotNull(message = "交易时间不能为空")
    private Long time;

    /**
     * 活动id
     */
    @NotBlank(message = "活动id不能为空")
    private String activityId;

    /**
     * 券核销码
     */
//    @NotBlank(message = "券核销码不能为空")
    private String writeOffCode;

    /**
     * 券使用状态  0 未生效  1 未使用 2 已使用  3 已失效  4 退回
     */
    @NotBlank(message = "券使用状态不能为空")
    private String status;

    /**
     * 用户编号
     */
    private String userNo;

    /**
     * 卡号
     */
    private String cardNumber;

}
