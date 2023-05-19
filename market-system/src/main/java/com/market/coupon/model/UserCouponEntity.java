package com.market.coupon.model;

import com.market.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户领取优惠券信息
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:19
 */
@Data
public class UserCouponEntity extends UserCouponInfo implements Serializable {
    private static final long serialVersionUID = -4853861602356991102L;

    /**
     * 优惠券名称
     */
    @Excel(name = "优惠券名称", sort = 0)
    private String couponName;

    /**
     *优惠券码
     */
    @Excel(name = "优惠券码", sort = 1)
    private String couponCode;

    /**
     *关联活动名称
     */
    @Excel(name = "关联活动", sort = 2)
    private String activityName;
}
