package com.market.activity.mapper;

import com.market.activity.model.GrantInfo;
import com.market.index.model.IndexParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-05 10:11
 */
public interface GrantMapper {

    /**
     * 发放记录数据入库
     * @param info
     * @return
     */
    int insert(GrantInfo info);

    /**
     * 获取优惠券发放数量
     * @param activityId
     * @param couponId
     * @return
     */
    List<GrantInfo> getDataList(@Param("activityId") String activityId,
                                @Param("couponId") String couponId);

    /**
     * 发放券数量
     * @param param
     * @return
     */
    int getSendNum(IndexParam param);
}
