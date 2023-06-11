package com.market.activity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.market.common.annotation.Excel;
import com.market.common.core.domain.BaseEntity;
import com.market.coupon.model.CouponTotal;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动信息对象 t_activity_info
 *
 * @author ph
 * @date 2023-04-15
 */
@Data
@Table(name = "T_ACTIVITY_INFO")
public class ActivityInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2995186463605191079L;

    /**
     *活动id
     */
    private String id;

    /**
     *活动简称
     */
    private String activityAbb;

    /**
     *活动名称
     */
    @Excel(name = "活动名称", sort = 1)
    private String activityName;

    /**
     *开始时间
     */
    @Excel(name = "开始时间", sort = 3, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     *结束时间
     */
    @Excel(name = "结束时间", sort = 4, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     *审批状态 0  待审核   1 审核通过   2 审核拒绝  3 待提交
     */
    @Excel(name = "审批状态", sort = 11, readConverterExp = "0=待审核,1=审核通过,2=审核拒绝,3=待提交")
    private String approveStatus;

    /**
     *活动总预算
     */
    @Excel(name = "活动总预算", sort = 6)
    private BigDecimal budget;

    /**
     *潇湘支付出资比例
     */
    @Excel(name = "潇湘支付出资比例", sort = 7)
    private BigDecimal xiaoxiangPayNum;

    /**
     *出资机构名称  0 银联  1 电信  2 长沙银行  3 中国移动
     */
    @Excel(name = "出资机构名称", sort = 8, readConverterExp = "0=银联,1=电信,2=长沙银行,3=中国移动")
    private String orgName;

    /**
     *机构出资比例
     */
    @Excel(name = "机构出资比例", sort = 9)
    private BigDecimal orgPayNum;

    /**
     *商家出资比例
     */
    @Excel(name = "商家出资比例", sort = 10)
    private BigDecimal merchantPayNum;

    /**
     *剩余资金预警比例
     */
    private BigDecimal alarmNum;

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
     *支付方式
     */
    private String payMethod;

    /**
     *有效时间
     */
    private String effectTime;

    /**
     *限制次数
     */
    private String limitNum;

    /**
     *使用规则
     */
    private String useRule;

    /**
     *缩略图
     */
    private String contractPhoto;

    /**
     *是否允许用户分享   0 否  1  是
     */
    private String shareFlag;

    /**
     *分享标题
     */
    private String shareTitle;

    /**
     *分享文案
     */
    private String shareContent;

    /**
     *分享图片
     */
    private String sharePhoto;

    /**
     *详情图
     */
    private String detailPhoto;

    /**
     *删除标识 0 未删除   1  删除
     */
    private String delFlag;

    /**
     *活动状态 0  未开始  1  进行中  2  已失效   3 已停止
     */
    @Excel(name = "活动状态", sort = 2, readConverterExp = "0=未开始,1=进行中,2=已失效,3=已停止")
    private String status;

    /**
     *审核备注
     */
    @Excel(name = "审核备注", sort = 12)
    private String auditRemark;

    /**
     *审核时间
     */
    @Excel(name = "审核时间", sort = 13, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /**
     * 优惠券id
     */
    @Transient
    private String couponId;

    /**
     *活动范围
     */
    @Excel(name = "活动范围", sort = 5)
    private String regionName;

    /**
     *活动范围区域代码
     */
    private String regionCode;

    /**
     *订单优惠总金额（元）
     */
    @Transient
    private BigDecimal orderAmount;

    /**
     *关联优惠券数
     */
    @Transient
    private Integer couponNum;

    /**
     *适用商户数量
     */
    @Transient
    private Integer merchantNum;

    /**
     *领取门槛  0  全部  1 新用户   2  首单  3  指定用户
     */
    @Transient
    private String receiveType;

    /**
     * 操作类型   0   新增   1  修改
     */
    private String operationType;

    /**
     * 修改人
     */
    @Transient
    private String updateName;

    /**
     * 关联优惠券列表
     */
    @Transient
    private List<ActivityCouponInfo> contactList;

    /**
     * 优惠券统计列表
     */
    @Transient
    private List<CouponTotal> totalList;

    /**
     * 商户列表
     */
    @Transient
    private List<MerchantInfo> merchantList;

    /**
     *券生效天数
     */
    @Transient
    private Integer takeDateNum;

    /**
     *券有效天数
     */
    @Transient
    private Integer effectDateNum;

    /**
     *活动内同类型可是否可以叠加 0 否  1  是
     */
    @Transient
    private String sameActivityFlag;

    /**
     *活动内不同类型可是否可以叠加 0 否  1  是
     */
    @Transient
    private String diffActivityFlag;

    /**
     *是否允许与其他活动的卡券叠加使用  0 否 1  是
     */
    @Transient
    private String outActivityFlag;

    /**
     *券使用有效期开始时间
     */
    @Transient
    private Date effectStartTime;

    /**
     *券使用有效期结束时间
     */
    @Transient
    private Date effectEndTime;

    /**
     *券使用有效期类型 0 固定日期  1  配置日期
     */
    @Transient
    private String effectType;

    /**
     *发券开始时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date grantStartTime;

    /**
     *发券结束时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date grantEndTime;

    /**
     * 活动简介
     */
    private String intro;

}
