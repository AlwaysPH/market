package com.market.activity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-03 17:53
 */
@Data
public class ActivityCouponParams implements Serializable {
    private static final long serialVersionUID = 8709292971129200511L;

    /**
     *活动ID
     */
    @NotNull(message = "活动ID不能为空")
    private String activityId;

    /**
     *领取门槛  0  全部  1 新用户   2  首单  3  指定用户
     */
    @NotNull(message = "领取门槛不能为空")
    private String receiveType;

    /**
     *发券开始时间
     */
    @NotNull(message = "发券开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date grantStartTime;

    /**
     *发券结束时间
     */
    @NotNull(message = "发券结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date grantEndTime;

    /**
     *券使用有效期类型 0 固定日期  1  配置日期
     */
    private String effectType;

    /**
     *券使用有效期开始时间（固定日期）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectStartTime;

    /**
     *券使用有效期结束时间（固定日期）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectEndTime;

    /**
     *券生效天数（配置日期）
     */
    private Integer takeDateNum;

    /**
     *券有效天数（配置日期）
     */
    private Integer effectDateNum;

    /**
     *剩余券库存预警
     */
    @NotNull(message = "剩余券库存预警不能为空")
    private Integer alarmNum;

    /**
     *预警内容
     */
    @NotNull(message = "预警内容不能为空")
    private String alarmContent;

    /**
     *预警手机号
     */
    @NotNull(message = "预警手机号不能为空")
    private String alarmPhone;

    /**
     *预警邮箱
     */
    @NotNull(message = "预警邮箱不能为空")
    private String alarmEmail;

    /**
     *活动内同类型可是否可以叠加 0 否  1  是
     */
    @NotNull(message = "活动内同类型可是否可以叠加不能为空")
    private String sameActivityFlag;

    /**
     *活动内不同类型可是否可以叠加 0 否  1  是
     */
    @NotNull(message = "活动内不同类型可是否可以叠加不能为空")
    private String diffActivityFlag;

    /**
     *是否允许与其他活动的卡券叠加使用  0 否 1  是
     */
    @NotNull(message = "允许与其他活动的卡券叠加使用不能为空")
    private String outActivityFlag;

    /**
     * 关联的优惠券列表
     */
    @NotNull(message = "关联的优惠券列表不能为空")
    List<ActivityCouponInfo> couponInfoList;

    /**
     * 指定用户列表
     */
    List<CardInfo> cardInfoList;

    /**
     * 配置商户列表
     */
    List<MerchantInfo> merchantList;

    /**
     * 修改标识  0 否  1  是
     */
    private String isEdit;

}
