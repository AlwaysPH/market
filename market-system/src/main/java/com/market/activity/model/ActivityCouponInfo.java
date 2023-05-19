package com.market.activity.model;

import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动关联优惠券信息对象 t_activity_coupon
 *
 * @author ruoyi
 * @date 2023-04-15
 */
@Data
@Table(name = "T_ACTIVITY_COUPON")
public class ActivityCouponInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3114496930631725388L;

    /**
     * 主键id
     */
    private String id;

    /**
     *活动ID
     */
    private String activityId;

    /**
     *优惠券ID
     */
    private String couponId;

    /**
     *库存数量
     */
    private Integer inventoryNum;

    /**
     *本次发放数量
     */
    private Integer grantNum;

    /**
     *券生效天数
     */
    private Integer takeDateNum;

    /**
     *券有效天数
     */
    private Integer effectDateNum;

    /**
     *活动内同类型可是否可以叠加 0 否  1  是
     */
    private String sameActivityFlag;

    /**
     *活动内不同类型可是否可以叠加 0 否  1  是
     */
    private String diffActivityFlag;

    /**
     *是否允许与其他活动的卡券叠加使用  0 否 1  是
     */
    private String outActivityFlag;

    /**
     *删除标识 0 未删除   1  删除
     */
    private String delFlag;

    /**
     *券使用有效期开始时间
     */
    private Date effectStartTime;

    /**
     *券使用有效期结束时间
     */
    private Date effectEndTime;

    /**
     *券使用有效期类型 0 固定日期  1  配置日期
     */
    private String effectType;

    /**
     *剩余券库存预警
     */
    private Integer alarmNum;

    /**
     *预警内容
     */
    private String alarmContent;

    /**
     *预警手机号
     */
    private String alarmPhone;

    /**
     *预警邮箱
     */
    private String alarmEmail;

    /**
     *领取门槛  0  全部  1 新用户   2  首单  3  指定用户
     */
    private String receiveType;

    private Integer nowGrantNum;

    /**
     * 优惠券可用状态 0 可用  1 不可用
     */
    @Transient
    private String availableStatus;

    /**
     * 领取数量
     */
    @Transient
    private Integer receiveNum;

    /**
     * 使用数量
     */
    @Transient
    private Integer useNum;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    @Transient
    private String couponType;

    /**
     * 优惠券名称
     */
    @Transient
    private String couponName;

    /**
     *优惠券码
     */
    @Transient
    private String couponCode;

    /**
     *优惠金额
     */
    @Transient
    private BigDecimal couponAmount;
}
