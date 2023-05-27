package com.market.coupon.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.annotation.Excel;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联优惠券信息
 * @author ph
 * @version 1.0
 * @date 2023-04-18 09:48
 */
@Data
@Table(name = "T_USER_COUPON")
public class UserCouponInfo implements Serializable {
    private static final long serialVersionUID = -307091629256396152L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 用户编号
     */
    private String userNo;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 券状态  0 未生效  1 未使用 2 已使用  3 已失效
     */
    @Excel(name = "优惠券使用状态", readConverterExp = "0=未生效,1=未使用,2=已使用,3=已失效", sort = 8)
    private String status;

    /**
     * 用户手机号
     */
    @Excel(name = "用户手机号", sort = 3)
    private String phoneNumber;

    /**
     * APP账号
     */
    @Excel(name = "APP账号", sort = 4)
    private String appAccount;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 设备号
     */
    private String deviceNo;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 领取时间
     */
    @Excel(name = "领取时间", dateFormat = "yyyy-MM-dd HH:mm:ss", sort = 5)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 券核销码
     */
    @Excel(name = "券核销码", sort = 6)
    private String writeOffCode;

    /**
     * 券链接
     */
    @Excel(name = "券链接", sort = 7)
    private String linkUrl;

    /**
     * 优惠券领取数量
     */
    @Transient
    private Integer receiveNum;

    /**
     *领取方式  1 自动发放  2 手动领取
     */
    private String sendType;

    /**
     *优惠券使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;

    /**
     *优惠券渠道  1 潇湘支付优惠券
     */
    private String channelType;
}
