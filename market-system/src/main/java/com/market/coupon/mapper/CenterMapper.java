package com.market.coupon.mapper;

import com.market.coupon.model.UserCouponEntity;
import com.market.coupon.model.UserCouponParam;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:26
 */
public interface CenterMapper {

    /**
     * 查询用户领取优惠券列表
     * @param param
     * @return
     */
    List<UserCouponEntity> getListData(UserCouponParam param);
}
