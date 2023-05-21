package com.market.activity.mapper;

import com.market.activity.model.ActivityCouponInfo;
import com.market.activity.model.GrantInfo;
import com.market.coupon.model.ActivityCouponEntity;
import com.market.coupon.model.CouponInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 活动关联优惠券信息Mapper接口
 *
 * @author ph
 * @date 2023-04-15
 */
public interface ActivityCouponMapper {

    /**
     * 查询活动已配置的优惠券
     * @param activityId
     * @param couponId
     * @param activityIds
     * @return
     */
    List<ActivityCouponInfo> getDataList(@Param("activityId") String activityId,
                                        @Param("couponId") String couponId,
                                        @Param("activityIds") List<String> activityIds);

    /**
     * 活动关联优惠券入库
     * @param list
     * @return
     */
    int insertData(List<ActivityCouponInfo> list);

    /**
     * 获取活动关联的优惠券列表
     * @param info
     * @return
     */
    List<ActivityCouponEntity> getDataListByCoupon(CouponInfo info);

    /**
     * 获取活动关联的优惠券详细列表
     * @param activityId
     * @return
     */
    List<GrantInfo> getCouponDetailList(String activityId);

    /**
     * 已发放优惠券信息
     * @param activityId
     * @param couponId
     * @return
     */
    List<GrantInfo> getGrantList(@Param("activityId") String activityId, @Param("couponId") String couponId);

    /**
     * 修改数据
     * @param activityCouponInfo
     */
    void update(ActivityCouponInfo activityCouponInfo);

    /**
     * 获取关联优惠券信息
     * @param activityId
     * @return
     */
    List<ActivityCouponInfo> getCouponDataList(@Param("activityId") String activityId);

    /**
     * 删除活动关联优惠券信息
     * @param list
     * @param userId
     * @return
     */
    int delete(@Param("list") List<String> list, @Param("userId") String userId);
}
