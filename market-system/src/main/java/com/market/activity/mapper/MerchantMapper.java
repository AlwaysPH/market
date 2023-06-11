package com.market.activity.mapper;

import com.market.activity.model.MerchantInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-05 20:19
 */
public interface MerchantMapper {

    /**
     * 商户数据入库
     * @param list
     * @return
     */
    int insertData(List<MerchantInfo> list);

    /**
     * 获取活动关联的商户
     * @param activityId
     * @return
     */
    List<MerchantInfo> getDataList(@Param("activityId") String activityId);

    /**
     * 获取商户列表
     * @param activityId
     * @param activityList
     * @return
     */
    List<MerchantInfo> getMerchantList(@Param("activityId") String activityId, @Param("activityList") List<String> activityList);

    /**
     * 删除活动关联商户信息
     * @param list
     * @param userId
     * @return
     */
    int delete(@Param("list") List<String> list, @Param("userId") String userId);
}
