package com.market.activity.mapper;

import com.market.activity.model.ActivityInfo;
import com.market.activity.model.CardInfo;
import com.market.coupon.model.AppParams;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 删除活动指定用户信息
     * @param list
     * @param userId
     * @return
     */
    int delete(@Param("list") List<String> list, @Param("userId") String userId);

    /**
     * 根据手机号获取指定用户关联的活动信息
     * @param phoneNumber
     * @return
     */
    List<ActivityInfo> getSpecialDataByPhone(String phoneNumber);

    /**
     * 获取活动指定用户
     * @param activityId
     * @return
     */
    List<CardInfo> getSpecialUser(String activityId);
}
