package com.market.activity.model;

import com.market.common.annotation.Excel;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 活动配置商户对象
 * @author ph
 * @version 1.0
 * @date 2023-05-05 14:30
 */
@Data
@Table(name = "T_ACTIVITY_MERCHANT")
public class MerchantInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6103796869951833728L;

    /**
     * id
     */
    private String id;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 商户编号
     */
    @Excel(name = "商户编号")
    private String merchantNo;

    /**
     * 商户名称
     */
    @Excel(name = "商户名称")
    private String merchantName;

    /**
     * 活动营销资金结算方式  0 足额结算  1 差额预存
     */
    @Excel(name = "活动营销资金结算方式", readConverterExp="0=足额结算,1=差额预存")
    private String settleType;

    /**
     * 行业类型  0 大型商场  1 大型超市  2 娱乐  3 影院 4 连锁便利店
     */
    @Excel(name = "行业类型", readConverterExp="0=大型商场,1=大型超市,2=娱乐,3=影院,4=连锁便利店")
    private String industryType;

    /**
     * 商户原消费费率
     */
    @Excel(name = "商户原消费费率")
    private Integer consumRate;

    /**
     * 活动期间商户消费费率
     */
    @Excel(name = "活动期间商户消费费率")
    private Integer activityConsumRate;

    /**
     *删除标识 0 未删除   1  删除
     */
    private String delFlag;

    /**
     *消费费率有效期开始时间
     */
    private Date effectStartTime;

    /**
     *消费费率有效期结束时间
     */
    private Date effectEndTime;

    /**
     * 参与活动数量
     */
    @Transient
    private Integer activityNum;
}
