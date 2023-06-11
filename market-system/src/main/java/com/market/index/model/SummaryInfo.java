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
    @Excel(name = "发放券数量", sort = 5)
    private Integer sendCouponNum;

    /**
     * 领取券数量
     */
    private Integer receiveCouponNum;

    /**
     * 领取率
     */
    @Excel(name = "领取率（%）", sort = 6)
    private Double receiveRate;

    /**
     * 使用率
     */
    @Excel(name = "使用率（%）", sort = 7)
    private Double useRate;

    /**
     * 交易总金额(使用该优惠券的总金额)
     */
    @Excel(name = "交易总金额（元）", sort = 9)
    private BigDecimal amount;

    /**
     * 优惠总金额(使用该优惠券的总金额)
     */
    @Excel(name = "优惠总金额（元）", sort = 8)
    private BigDecimal disAmount;

    /**
     * 用券总单量(使用优惠券的付款订单总数)
     */
    @Excel(name = "用券总单量", sort = 10)
    private Integer orderNum;

    /**
     * 用券笔单价(用券总成交额/使用优惠券的付款订单总数)
     */
    @Excel(name = "用券笔单价（元）", sort = 11)
    private BigDecimal orderPrice;

    /**
     * 拉新数（领取过优惠券的用户中，标记为新用户的数量）
     */
    @Excel(name = "拉新数", sort = 12)
    private Integer newNum;

    /**
     * 使用数
     */
    @Excel(name = "使用数", sort = 13)
    private Integer useNum;

    /**
     * 优惠券名称
     */
    @Excel(name = "优惠券名称", sort = 1)
    private String couponName;

    /**
     *优惠券码
     */
    @Excel(name = "优惠券码", sort = 2)
    private String couponCode;

    /**
     *活动id
     */
    private String activityId;

    /**
     *活动名称
     */
    @Excel(name = "活动名称", sort = 3)
    private String activityName;

    /**
     *活动总预算
     */
    @Excel(name = "活动总预算（元）", sort = 4)
    private BigDecimal budget;

    /**
     * 优惠券id
     */
    private String couponId;
}
