package com.market.coupon.mapper;

import com.market.activity.model.ActivityInfo;
import com.market.coupon.model.AppParams;
import com.market.coupon.model.CouponThreshold;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-19 15:41
 */
public interface CouponThresholdMapper {
    /**
     * 优惠券使用门槛批量插入
     * @param list
     * @return
     */
    int batchInsertData(List<CouponThreshold> list);

    /**
     * 获取优惠券门槛信息
     * @param couponId
     * @param idList
     * @return
     */
    List<CouponThreshold> getDataByCouponId(@Param("couponId") String couponId,
                                            @Param("idList") List<String> idList);

    /**
     * 删除优惠券门槛信息
     * @param couponId
     * @return
     */
    int deleteByCouponId(@Param("couponId") String couponId);

    /**
     * 修改
     * @param list
     * @param updateUser
     * @return
     */
    int updateByCouponId(@Param("list") List<String> list, @Param("updateUser") String updateUser);

    /**
     * 获取APP的活动关联优惠券列表
     * @param info
     * @return
     */
    List<CouponThreshold> getAppActivityCouponList(ActivityInfo info);

    /**
     * 获取用户领取的优惠券
     * @param params
     * @return
     */
    List<CouponThreshold> getUserCouponList(AppParams params);
}
