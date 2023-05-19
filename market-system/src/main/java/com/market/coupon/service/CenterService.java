package com.market.coupon.service;

import com.market.coupon.model.UserCouponEntity;
import com.market.coupon.model.UserCouponParam;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:12
 */
public interface CenterService {

    /**
     * 查询用户领取优惠券列表
     * @param param
     * @return
     */
    List<UserCouponEntity> list(UserCouponParam param);
}
