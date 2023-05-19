package com.market.activity.model;

import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 活动关联优惠券发放记录信息
 * @author ph
 * @version 1.0
 * @date 2023-05-05 09:23
 */
@Data
@Table(name = "T_GRANT_INFO")
public class GrantInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -9045909245345374834L;

    /**
     * id
     */
    private String id;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 发放数量
     */
    private Integer grantNum;

    /**
     * 优惠券名称
     */
    @Transient
    private String couponName;

    /**
     * 库存数量
     */
    @Transient
    private Integer inventoryNum;
}
