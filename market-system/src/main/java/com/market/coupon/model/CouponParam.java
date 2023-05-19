package com.market.coupon.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-19 16:52
 */
@Data
public class CouponParam extends CouponInfo implements Serializable {

    /**
     *满减券限制类型（0 无限制， 1 固定满减，2 阶梯满减）
     */
    private String restrictionType;

    /**
     *满减条件(阶梯满减)
     */
    private BigDecimal fullSubCondition;
    /**
     *满减金额(阶梯满减)
     */
    private BigDecimal fullSubAmount;

    /**
     *满减条件(固定满减)
     */
    private BigDecimal fullFixSubCondition;

    /**
     *优惠金额(固定满减)
     */
    private BigDecimal disFixAmount;

    /**
     *优惠金额(无限制)
     */
    private BigDecimal disAmount;

    /**
     *满额限制
     */
    private BigDecimal fullRestriction;

    /**
     *折扣比例
     */
    private BigDecimal saleNum;

    /**
     *是否有上限  0 否  1  是
     */
    private String upperLimitFlag;

    /**
     *上限满额金额
     */
    private BigDecimal upperLimitAmount;

    /**
     *面值
     */
    private BigDecimal faceMoney;

    /**
     *订单需满
     */
    private BigDecimal orderAmount;

    /**
     *购买金额
     */
    private BigDecimal payAmount;
}
