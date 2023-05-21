package com.market.coupon.mapper;

import com.market.coupon.model.*;
import com.market.index.model.IndexParam;
import com.market.index.model.SummaryInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-16 21:28
 */
 public interface CouponMapper {

    /**
     * 查询优惠券信息
     *
     * @param id 优惠券信息主键
     * @return 优惠券信息
     */
     CouponInfo selectCouponInfoById(String id);

    /**
     * 查询优惠券信息列表
     *
     * @param info 优惠券信息
     * @return 优惠券信息集合
     */
     List<CouponInfo> selectCouponInfoList(CouponInfo info);

    /**
     * 新增优惠券信息
     *
     * @param info 优惠券信息
     * @return 结果
     */
     int insertCouponInfo(CouponInfo info);

    /**
     * 修改优惠券信息
     *
     * @param info 优惠券信息
     * @return 结果
     */
     int updateCouponInfo(CouponInfo info);

    /**
     * 批量删除优惠券信息
     * @param list
     * @param updateUser
     * @return
     */
     int deleteCouponInfoByIds(@Param("list") List<String> list, @Param("updateUser") String updateUser);

    /**
     * 获取优惠券关联活动信息
     * @param list
     * @return
     */
    List<ActivityCoupon> selectActivityCouponList(@Param("list") List<String> list);

    /**
     * 获取优惠券信息
     * @param info
     * @return
     */
    List<ActivityCouponEntity> selectInfoList(CouponInfo info);

    /**
     * 获取优惠券信息
     * @param list
     * @return
     */
    List<CouponInfo> getListData(@Param("list") List<String> list);

    /**
     * 获取优惠券领取数据
     * @param idList
     * @return
     */
    List<UserCouponInfo> getReceiveList(@Param("list") List<String> idList);

    /**
     * 获取优惠券关联活动数据
     * @param idList
     * @return
     */
    List<ActivityCoupon> getActivityCouponList(List<String> idList);

    /**
     * 获取已领取的优惠券
     * @param activityId
     * @param couponId
     * @return
     */
    List<UserCouponInfo> getReceiveDataList(@Param("activityId") String activityId,
                                            @Param("couponId") String couponId);

    /**
     * 领取券数量
     * @param param
     * @return
     */
    int getReceiveNum(IndexParam param);

    /**
     * 使用券数量
     * @param param
     * @return
     */
    int getUseNum(IndexParam param);

    /**
     * 获取统计列表数据
     * @param param
     * @return
     */
    List<SummaryInfo> getSummaryListData(IndexParam param);

    /**
     * 获取优惠券使用数据
     * @param param
     * @return
     */
    List<SummaryInfo> getUseNumByCouponIds(IndexParam param);

   /**
    * 获取优惠券关联活动数量
    * @param id
    * @return
    */
    int getActivityNum(String id);

    /**
     * 获取当前用户领取优惠券数量
     * @param params
     * @return
     */
    int getPersonReceiveNum(AppParams params);

    /**
     * 领券
     * @param info
     */
    void insertCouponUser(UserCouponInfo info);

    /**
     * 获取我的优惠券列表
     * @param params
     * @return
     */
    List<CouponThreshold> getUserCouponList(AppParams params);

    /**
     * 获取用户关联优惠券
     * @param params
     * @return
     */
    UserCouponInfo getUserCouponData(AppParams params);

    /**
     * 更新用户关联优惠券
     * @param info
     * @return
     */
    int updateUserCoupon(UserCouponInfo info);

    /**
     * 获取还未使用的优惠券列表
     * @return
     * @param status
     */
    List<CouponTimeOut> getTimeOutList(@Param("list") List<String> status);

    /**
     * 更新已失效的优惠券
     * @param list
     * @param status
     */
    void updateTimeOut(@Param("list") List<String> list, @Param("status") String status);
}
