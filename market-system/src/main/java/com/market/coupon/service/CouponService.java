package com.market.coupon.service;

import com.market.coupon.model.*;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-16 21:20
 */
 public interface CouponService {

    /**
     * 查询优惠券信息
     *
     * @param id 优惠券信息主键
     * @return 优惠券信息
     */
    CouponParam selectCouponInfoById(String id);

    /**
     * 查询优惠券信息列表
     *
     * @param info 优惠券信息
     * @return 优惠券信息集合
     */
     List<ActivityCouponEntity> selectCouponInfoList(CouponInfo info);

    /**
     * 新增优惠券信息
     *
     * @param param 优惠券信息
     * @return 结果
     */
     int insertCouponInfo(CouponParam param);

    /**
     * 修改优惠券信息
     *
     * @param param 优惠券信息
     * @return 结果
     */
     int updateCouponInfo(CouponParam param);

    /**
     * 批量删除优惠券信息
     *
     * @param ids 需要删除的优惠券信息主键集合
     * @return 结果
     */
     int deleteCouponInfoByIds(String[] ids);

    /**
     * 获取新增关联的优惠券列表
     * @param info
     * @return
     */
    List<CouponEntity> getCouponDetailList(CouponInfo info);

    /**
     * 获取活动关联的优惠券列表
     * @param info
     * @return
     */
    List<ActivityCouponEntity> getActivityCouponList(CouponInfo info);

    /**
     * 获取我的优惠券列表
     * @return
     * @param params
     */
    List<CouponThreshold> getUserCouponList(AppParams params);

    /**
     * 更新优惠券使用状态
      * @param params
     * @return
     */
    int useCoupon(AppParams params);
}
