package com.market.coupon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 优惠券使用门槛信息对象 t_coupon_threshold
 *
 * @author ruoyi
 * @date 2023-04-15
 */
@Data
@Table(name = "T_COUPON_THRESHOLD")
public class CouponThreshold extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5459667107162530888L;

    /**
     *主键id
     */
    private String id;

    /**
     *优惠券id
     */
    private String couponId;

    /**
     *优惠券类型（1 满减  2 折扣  3 代金券）
     */
    private String couponType;

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

    /**
     *删除标识  0 未删除  1 已删除
     */
    private String delFlag;

    /**
     * 优惠券名称
     */
    @Transient
    private String couponName;

    /**
     *领取门槛  0  全部  1 新用户   2  首单  3  指定用户
     */
    @Transient
    private String receiveType;

    /**
     *券使用有效期类型 0 固定日期  1  配置日期
     */
    @Transient
    private String effectType;

    /**
     *券使用有效期开始时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectStartTime;

    /**
     *券使用有效期结束时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectEndTime;

    /**
     *券生效天数
     */
    @Transient
    private Integer takeDateNum;

    /**
     *券有效天数
     */
    @Transient
    private Integer effectDateNum;

    /**
     *活动内同类型可是否可以叠加 0 否  1  是
     */
    @Transient
    private String sameActivityFlag;

    /**
     *活动内不同类型可是否可以叠加 0 否  1  是
     */
    @Transient
    private String diffActivityFlag;

    /**
     *是否允许与其他活动的卡券叠加使用  0 否 1  是
     */
    @Transient
    private String outActivityFlag;

    /**
     *使用说明
     */
    @Transient
    private String useDesc;

    /**
     *条件说明
     */
    @Transient
    private String conditionDesc;

    /**
     * 活动名称
     */
    @Transient
    private String activityName;

    /**
     * 领取时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 券状态  0 未生效  1 未使用 2 已使用  3 已失效
     */
    @Transient
    private String status;

    /**
     * 活动id
     */
    @Transient
    private String activityId;

    /**
     *优惠券使用日期
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;

    /**
     * 发放数量
     */
    @Transient
    private Integer grantNum;

    /**
     * 领取数量
     */
    @Transient
    private Integer receiveNum;

    /**
     * 剩余数量
     */
    @Transient
    private Integer surplusNum;

    /**
     * 库存不足标识   0   否   1  是
     */
    @Transient
    private Integer fullFlag;

    /**
     * 阶梯满减券使用门槛
     */
    @Transient
    private List<CouponThreshold> ladderList;

    /**
     * 用户领取优惠券表id
     */
    private String userCouponId;

    /**
     *优惠券渠道  1 潇湘支付优惠券
     */
    @Transient
    private String channelType;
}
