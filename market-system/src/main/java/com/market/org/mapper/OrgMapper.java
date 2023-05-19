package com.market.org.mapper;

import com.market.org.model.OrgInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-15 10:35
 */
public interface OrgMapper {

    /**
     * 查询机构
     *
     * @param id 机构主键
     * @return 机构
     */
    OrgInfo selectOrgInfoById(String id);

    /**
     * 查询机构列表
     *
     * @param orgInfo 机构
     * @return 机构集合
     */
    List<OrgInfo> selectOrgInfoList(OrgInfo orgInfo);

    /**
     * 新增机构
     *
     * @param orgInfo 机构
     * @return 结果
     */
    int insertOrgInfo(OrgInfo orgInfo);

    /**
     * 修改机构
     *
     * @param orgInfo 机构
     * @return 结果
     */
    int updateOrgInfo(OrgInfo orgInfo);

    /**
     * 批量删除机构
     * @param list
     * @param updateUser
     * @return
     */
    int deleteOrgInfoByIds(@Param("list") List<String> list, @Param("updateUser") String updateUser);

    /**
     * 根据机构名获取机构信息
     * @param nameList
     * @return
     */
    List<OrgInfo> getListDataByName(@Param("list") List<String> nameList);

    /**
     * 批量插入数据
     * @param data
     */
    void batchInsert(@Param("list") List<OrgInfo> data);
}
