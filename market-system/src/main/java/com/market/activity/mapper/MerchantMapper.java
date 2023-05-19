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
     * @return
     */
    List<MerchantInfo> getMerchantList(@Param("activityId") String activityId);
}
