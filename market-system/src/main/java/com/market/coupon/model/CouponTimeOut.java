package com.market.coupon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-21 17:04
 */
@Data
public class CouponTimeOut implements Serializable {
    private static final long serialVersionUID = -7898857779710756649L;

    /**
     * 用户关联优惠券id
     */
    private String userCouponId;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 领取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

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
     *活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     *活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
