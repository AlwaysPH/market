package com.market.index.model;

import com.market.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 14:01
 */
@Data
public class SummaryInfo implements Serializable {
    private static final long serialVersionUID = 1082195619690821586L;

    /**
     * 活动数
     */
    private Integer activityNum;

    /**
     * 发放券数量
     */
    private Integer sendCouponNum;

    /**
     * 领取券数量
     */
    private Integer receiveCouponNum;

    /**
     * 领取率
     */
    private Double receiveRate;

    /**
     * 使用率
     */
    private Double useRate;

    /**
     * 交易总金额(使用该优惠券的总金额)
     */
    private BigDecimal amount;

    /**
     * 优惠金额(使用该优惠券的总金额)
     */
    private BigDecimal disAmount;

    /**
     * 用券总单量(使用优惠券的付款订单总数)
     */
    private Integer orderNum;

    /**
     * 用券笔单价(用券总成交额/使用优惠券的付款订单总数)
     */
    private BigDecimal orderPrice;

    /**
     * 拉新数（领取过优惠券的用户中，标记为新用户的数量）
     */
    private Integer newNum;

    /**
     * 使用数
     */
    private Integer useNum;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     *优惠券码
     */
    private String couponCode;

    /**
     *活动id
     */
    private String activityId;

    /**
     *活动名称
     */
    private String activityName;

    /**
     *活动总预算
     */
    private BigDecimal budget;

    /**
     * 优惠券id
     */
    private String couponId;
}
