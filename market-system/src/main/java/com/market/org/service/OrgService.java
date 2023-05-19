package com.market.org.service;

import com.market.common.utils.poi.ExcelError;
import com.market.org.model.OrgInfo;
import com.market.org.model.OrgParam;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-15 10:28
 */
public interface OrgService {

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
     * @param param 机构
     * @return 结果
     */
    int insertOrgInfo(OrgParam param);

    /**
     * 修改机构
     *
     * @param orgInfo 机构
     * @return 结果
     */
    int updateOrgInfo(OrgInfo orgInfo);

    /**
     * 批量删除机构
     *
     * @param ids 需要删除的机构主键集合
     * @return 结果
     */
    int deleteOrgInfoByIds(String[] ids);

    /**
     * 数据导入
     * @param list
     * @param sb
     * @return
     */
    List<OrgInfo> importData(List<OrgParam> list, StringBuilder sb);

    /**
     * 批量插入数据
     * @param data
     */
    void batchInsertData(List<OrgInfo> data);
}
