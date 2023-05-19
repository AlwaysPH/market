package com.market.coupon.service.impl;

import com.market.coupon.mapper.CenterMapper;
import com.market.coupon.model.UserCouponEntity;
import com.market.coupon.model.UserCouponParam;
import com.market.coupon.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-20 11:25
 */
@Service
public class CenterServiceImpl implements CenterService {

    @Autowired
    private CenterMapper centerMapper;

    /**
     * 查询用户领取优惠券列表
     * @param param
     * @return
     */
    @Override
    public List<UserCouponEntity> list(UserCouponParam param) {
        return centerMapper.getListData(param);
    }
}
