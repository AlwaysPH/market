package com.market.coupon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券使用明细
 * @author ph
 * @version 1.0
 * @date 2023-05-22 10:01
 */
@Data
@Table(name = "T_COUPON_USE_DETAIL")
public class CouponUseEntity extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    private String id;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 用户关联优惠券id
     */
    private String userCouponId;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 交易金额(使用该优惠券的订单付款总额)
     */
    private BigDecimal orderAmount;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 优惠金额(使用该优惠券的总金额)
     */
    private BigDecimal disAmount;

    /**
     * 标记是否是新用户  0 否  1 是
     */
    private Integer userType;

    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;

    /**
     * 用券总单量(使用优惠券的付款订单总数)
     */
    @Transient
    private Integer orderNum;

    /**
     * 用券笔单价(用券总成交额/使用优惠券的付款订单总数)
     */
    @Transient
    private BigDecimal orderPrice;

    /**
     * 拉新数（领取过优惠券的用户中，标记为新用户的数量）
     */
    @Transient
    private Integer newNum;

    /**
     *优惠券渠道  1 潇湘支付优惠券
     */
    private String channelType;
}
