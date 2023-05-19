package com.market.activity.mapper;

import com.market.activity.model.CardInfo;
import com.market.coupon.model.AppParams;

import java.util.List;


/**
 * 活动关联优惠券指定用户
 *
 * @author ph
 * @date 2023-04-15
 */
public interface ActivitySpecialUserMapper {

    /**
     * 活动关联优惠券指定用户入库
     * @param list
     * @return
     */
    int insertData(List<CardInfo> list);

    /**
     * 判断当前活动是否关联指定用户
     * @param params
     * @return
     */
    int getSpecialData(AppParams params);
}
