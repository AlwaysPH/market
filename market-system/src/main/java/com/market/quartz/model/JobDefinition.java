package com.market.quartz.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-06 11:18
 */
@Data
public class JobDefinition implements Serializable {
    private static final long serialVersionUID = -7291629001736347391L;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     *领取门槛  0  全部  1 新用户   2  首单  3  指定用户
     */
    private String receiveType;

}
