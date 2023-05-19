package com.market.coupon.model;

import com.market.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-18 08:59
 */
@Data
public class ActivityCouponEntity implements Serializable {

    private static final long serialVersionUID = -8935118998904837926L;

    /**
     * 优惠券id
     */
    private String id;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    @Excel(name = "优惠券类型", readConverterExp = "0=满减券,1=折扣券,2=代金券")
    private String couponType;

    /**
     * 优惠券名称
     */
    @Excel(name = "优惠券名称")
    private String couponName;

    /**
     *优惠券码
     */
    @Excel(name = "优惠券码")
    private String couponCode;

    /**
     *关联活动数量
     */
    @Excel(name = "关联活动数量")
    private Integer contactNum;

    /**
     *券图片
     */
    private String couponPhoto;
}
