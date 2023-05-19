package com.market.coupon.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 券统计
 * @author ph
 * @version 1.0
 * @date 2023-05-07 16:49
 */
@Data
public class CouponTotal implements Serializable {
    private static final long serialVersionUID = 8586553912046611461L;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    private String couponType;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    private String couponTypeName;

    /**
     * 本活动累计发放张数
     */
    private Integer grantNum;

    /**
     * 发放总优惠
     */
    private BigDecimal grantAmount;

    /**
     * 本活动累计领取张数
     */
    private Integer receiveNum;

    /**
     * 领取总优惠
     */
    private BigDecimal receiveAmount;

    /**
     * 本活动累计使用总张数
     */
    private Integer useNum;

    /**
     * 使用总优惠
     */
    private BigDecimal useAmount;

}
