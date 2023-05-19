package com.market.coupon.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-28 13:59
 */
@Data
public class CouponEntity implements Serializable {
    private static final long serialVersionUID = -5138610297802686516L;

    /**
     * 优惠券id
     */
    private String id;

    /**
     * 业务划分 0 多用途
     */
    private String businessType;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    private String couponType;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 优惠券金额
     */
    private BigDecimal couponAmount;

    /**
     * 库存数量
     */
    private Integer inventoryNum;

    /**
     * 已发放数量
     */
    private Integer grantNum;

    /**
     * 原剩余库存
     */
    private Integer surplus;

    /**
     * 本次发放数量
     */
    private Integer nowGrantNum;

    /**
     * 本次发放后剩余库存
     */
    private Integer nowSurplus;
}
