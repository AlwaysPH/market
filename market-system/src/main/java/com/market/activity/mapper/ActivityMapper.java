package com.market.activity.mapper;

import com.market.activity.model.ActivityInfo;
import com.market.index.model.IndexParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-18 15:42
 */
public interface ActivityMapper {

    /**
     * 查询活动信息
     *
     * @param id 活动信息主键
     * @return 活动信息
     */
    ActivityInfo selectActivityInfoById(String id);

    /**
     * 查询活动信息列表
     *
     * @param info 活动信息
     * @return 活动信息集合
     */
    List<ActivityInfo> selectActivityInfoList(ActivityInfo info);

    /**
     * 新增活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    int insertActivityInfo(ActivityInfo info);

    /**
     * 修改活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    int updateActivityInfo(ActivityInfo info);

    /**
     * 批量删除活动信息
     *
     * @param list 需要删除的数据主键集合
     * @param userId
     * @return 结果
     */
    int deleteActivityInfoByIds(@Param("list") List<String> list, @Param("userId") String userId);

    /**
     * 查询优惠券关联活动列表
     * @param info
     * @return
     */
    List<ActivityInfo> getListByCoupon(ActivityInfo info);

    /**
     * 活动数
     * @param param
     * @return
     */
    int getActivityNum(IndexParam param);

    /**
     * 获取APP的活动列表
     * @return
     */
    List<ActivityInfo> getAppActivityList();

}
