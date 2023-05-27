package com.market.index.model;

import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 14:06
 */
@Data
public class IndexParam extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8229076534703599014L;

    /**
     * 统计开始时间
     */
    private String startTime;

    /**
     * 统计结束时间
     */
    private String endTime;

    /**
     *活动名称
     */
    private String activityName;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     *优惠券码
     */
    private String couponCode;

    /**
     * 活动开始时间
     */
    private String beginTime;

    /**
     * 优惠券id列表
     */
    private List<String> couponIdList;

    /**
     * 活动id列表
     */
    private List<String> activityIdList;
}
