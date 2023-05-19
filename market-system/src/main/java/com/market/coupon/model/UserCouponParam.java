package com.market.coupon.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:14
 */
@Data
public class UserCouponParam implements Serializable {
    private static final long serialVersionUID = -4901280507398812378L;

    /**
     *APP账号/手机号
     */
    private String account;

    /**
     *优惠券名称
     */
    private String couponName;

    /**
     *优惠券码
     */
    private String couponCode;

    /**
     *券使用状态 0 未生效  1 未使用 2 已使用  3 已失效
     */
    private String status;

    /**
     *业务划分 0 多用途
     */
    private String businessType;

    /**
     *优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    private String couponType;
}
