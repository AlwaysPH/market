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
     * 交易总金额
     */
    private BigDecimal amount;

    /**
     * 用券总单量
     */
    private Integer orderNum;

    /**
     * 用券笔单价
     */
    private Integer orderPrice;

    /**
     * 拉新数
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
