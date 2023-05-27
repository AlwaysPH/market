package com.market.coupon.model;

import com.market.common.annotation.Excel;
import com.market.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 优惠券信息对象 t_coupon_info
 *
 * @author ph
 * @date 2023-04-15
 */
@Data
@Table(name = "T_COUPON_INFO")
public class CouponInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7921119592845932901L;
    /**
     * 优惠券id
     */
    private String id;

    /**
     * 业务划分 0 多用途
     */
    @Excel(name = "业务划分", readConverterExp = "0=多用途")
    @NotNull(message = "业务划分不能为空")
    private String businessType;

    /**
     * 优惠券类型 0 满减券 1 折扣券  2 代金券
     */
    @Excel(name = "优惠券类型", readConverterExp = "0=满减券,1=折扣券,2=代金券")
    @NotNull(message = "优惠券类型不能为空")
    private String couponType;

    /**
     * 优惠券简称
     */
    @Excel(name = "优惠券简称")
    @NotNull(message = "优惠券简称不能为空")
    private String couponAdd;

    /**
     * 优惠券名称
     */
    @Excel(name = "优惠券名称")
    @NotNull(message = "优惠券名称不能为空")
    private String couponName;

    /**
     *优惠券码
     */
    @Excel(name = "优惠券码")
    private String couponCode;

    /**
     *优惠券配图
     */
    @NotNull(message = "优惠券配图不能为空")
    private String couponPhoto;

    /**
     *使用说明
     */
    @Excel(name = "使用说明")
    @NotNull(message = "使用说明不能为空")
    private String useDesc;

    /**
     *核销渠道 0 二维码
     */
    @Excel(name = "核销渠道", readConverterExp = "0=二维码")
    @NotNull(message = "核销渠道不能为空")
    private String verChannel;

    /**
     *支付类型 0 APP扫码
     */
    @Excel(name = "支付类型", readConverterExp = "0=APP扫码")
    @NotNull(message = "支付类型不能为空")
    private String payType;

    /**
     *删除标识 0 未删除  1  删除
     */
    private String delFlag;

    /**
     *领取方式  1 自动发放  2 手动领取
     */
    @NotNull(message = "领取方式不能为空")
    private String sendType;

    /**
     *条件说明
     */
    @NotNull(message = "条件说明不能为空")
    private String conditionDesc;

    /**
     *每人限领
     */
    @NotNull(message = "每人限领不能为空")
    private Integer personLimit;

    /**
     *每日限领
     */
    @NotNull(message = "每日限领不能为空")
    private Integer dateLimit;

    /**
     *每台限领
     */
    @NotNull(message = "每台限领不能为空")
    private Integer machineLimit;

    /**
     *优惠券使用门槛(阶梯满减)
     */
    @Transient
    private List<CouponThreshold> conditionList;

    /**
     * 活动id
     */
    @Transient
    private String activityId;

    @Transient
    private String startTime;

    @Transient
    private String endTime;

    /**
     *优惠券渠道  1 潇湘支付优惠券
     */
    @NotNull(message = "优惠券渠道不能为空")
    private String channelType;
}
