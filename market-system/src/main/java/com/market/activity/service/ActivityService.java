package com.market.activity.service;

import com.market.activity.model.*;
import com.market.common.core.domain.AjaxResult;
import com.market.coupon.model.AppParams;
import com.market.coupon.model.CouponThreshold;

import java.util.List;

/**
 * 活动信息Service接口
 *
 * @author ph
 * @date 2023-04-15
 */
public interface ActivityService {
    /**
     * 查询活动信息
     *
     * @param id 活动信息主键
     * @return 活动信息
     */
    ActivityInfo selectActivityInfoById(String id);

    /**
     * 查询活动信息列表
     *
     * @param info 活动信息
     * @return 活动信息集合
     */
    List<ActivityInfo> selectActivityInfoList(ActivityInfo info);

    /**
     * 新增活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    int insertActivityInfo(ActivityInfo info);

    /**
     * 修改活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    int updateActivityInfo(ActivityInfo info);

    /**
     * 批量删除活动信息
     *
     * @param ids 需要删除的活动信息主键集合
     * @return 结果
     */
    int deleteActivityInfoByIds(String[] ids);

    /**
     * 查询优惠券关联活动列表
     * @param info
     * @return
     */
    List<ActivityInfo> getListByCoupon(ActivityInfo info);

    /**
     * 审核活动信息
     * @param info
     * @return
     */
    int audit(ActivityInfo info);

    /**
     * 提交审核
     * @param info
     * @return
     */
    int submitAudit(ActivityInfo info);

    /**
     * 配置优惠券
     * @param params
     * @return
     */
    int submitCoupon(ActivityCouponParams params);

    /**
     * 指定用户
     * @param params
     * @return
     */
    int specifyUser(ActivityCouponParams params);

    /**
     * 获取活动关联优惠券信息
     * @param activityId
     * @return
     */
    List<GrantInfo> getGrantCoupon(String activityId);

    /**
     * 发放优惠券
     * @param info
     * @return
     */
    int grantCoupon(GrantInfo info);

    /**
     * 卡分类数据导入
     * @param list
     * @param sb
     * @param activityId
     * @return
     */
    List<CardInfo> importData(List<CardInfo> list, StringBuilder sb, String activityId);

    /**
     * 卡分类数据入库
     * @param data
     */
    void batchInsertCardData(List<CardInfo> data);

    /**
     * 配置商户
     * @param params
     * @return
     */
    int configMerchant(ActivityCouponParams params);

    /**
     * 商户数据导入
     * @param list
     * @param sb
     * @param activityId
     * @return
     */
    List<MerchantInfo> importMerchantData(List<MerchantInfo> list, StringBuilder sb, String activityId);

    /**
     * 商户数据入库
     * @param data
     */
    void batchInsertMerchantData(List<MerchantInfo> data);

    /**
     * 停止活动
     * @param params
     * @return
     */
    int stop(ActivityCouponParams params);

    /**
     * 重启活动
     * @param params
     * @return
     */
    int restart(ActivityCouponParams params);

    /**
     * 获取APP的活动列表
     * @return
     * @param params
     */
    List<ActivityInfo> getAppActivityList(AppParams params);

    /**
     * 获取APP的活动关联优惠券列表
     * @param info
     * @return
     */
    List<CouponThreshold> getAppActivityCouponList(ActivityInfo info);

    /**
     * 领取优惠券
     * @param params
     * @return
     */
    AjaxResult receiveCoupon(AppParams params);

    /**
     * 获取APP优惠券使用门店
     * @param params
     * @return
     */
    List<MerchantInfo> getAPPCouponMerchant(AppParams params);

    /**
     * 获取活动详情
     * @param params
     * @return
     */
    AjaxResult getAPPActivityDetail(AppParams params);

    /**
     * 获取卡属性用户信息
     * @param cardNo
     * @return
     */
    CardInfo getCardUserInfo(String cardNo);

    /**
     * 获取商户列表
     * @param industryType
     * @return
     */
    List<MerchantInfo> getMerchantList(String industryType);

    /**
     * 获取活动已配置的商户信息
     * @param activityId
     * @return
     */
    List<MerchantInfo> getConfigMerchant(String activityId);
}
