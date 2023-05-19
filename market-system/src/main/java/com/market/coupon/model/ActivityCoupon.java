package com.market.coupon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 活动关联优惠券信息对象 t_activity_coupon
 *
 * @author ph
 * @date 2023-04-15
 */
@Data
@Table(name = "T_ACTIVITY_COUPON")
public class ActivityCoupon extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4420484304229599872L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 活动ID
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
     *券使用有效期类型 0 固定日期  1  配置日期
     */
    private String effectType;

    /**
     *券使用有效期开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectStartTime;

    /**
     *券使用有效期结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectEndTime;

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
     *活动外是否可以叠加  0 否 1  是
     */
    private String outActivityFlag;

    /**
     *删除标识 0 未删除   1  删除
     */
    private String delFlag;

    /**
     * 券领取数量
     */
    @Transient
    private Integer receiveNum;

}
