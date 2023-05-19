package com.market.org.service.impl;

import com.google.common.collect.Lists;
import com.market.common.core.domain.entity.SysUser;
import com.market.common.enums.AuditStatusEnum;
import com.market.common.exception.ServiceException;
import com.market.common.utils.CommonUtils;
import com.market.common.utils.DateUtils;
import com.market.common.utils.RegexCommon;
import com.market.common.utils.SecurityUtils;
import com.market.common.utils.poi.ExcelError;
import com.market.common.utils.uuid.IdUtils;
import com.market.org.mapper.OrgMapper;
import com.market.org.model.OrgInfo;
import com.market.org.model.OrgParam;
import com.market.org.service.OrgService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-15 10:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgMapper orgMapper;

    /**
     * 查询机构
     *
     * @param id 机构主键
     * @return 机构
     */
    @Override
    public OrgInfo selectOrgInfoById(String id) {
        return orgMapper.selectOrgInfoById(id);
    }

    /**
     * 查询机构列表
     *
     * @param orgInfo 机构
     * @return 机构
     */
    @Override
    public List<OrgInfo> selectOrgInfoList(OrgInfo orgInfo) {
        return orgMapper.selectOrgInfoList(orgInfo);
    }

    /**
     * 新增机构
     *
     * @param param 机构
     * @return 结果
     */
    @Override
    public int insertOrgInfo(OrgParam param) {
        OrgInfo orgInfo = new OrgInfo();
        BeanUtils.copyProperties(param, orgInfo);
        orgInfo.setId(IdUtils.simpleUUID());
        orgInfo.setCreateTime(DateUtils.getNowDate());
        orgInfo.setDelFlag("0");
        orgInfo.setCreateBy(SecurityUtils.getUserId());
        orgInfo.setOrgCode(CommonUtils.getRandom(8));
        return orgMapper.insertOrgInfo(orgInfo);
    }

    /**
     * 修改机构
     *
     * @param orgInfo 机构
     * @return 结果
     */
    @Override
    public int updateOrgInfo(OrgInfo orgInfo) {
        orgInfo.setUpdateTime(DateUtils.getNowDate());
        orgInfo.setUpdateBy(SecurityUtils.getUserId());
        return orgMapper.updateOrgInfo(orgInfo);
    }

    /**
     * 批量删除机构
     *
     * @param ids 需要删除的机构主键
     * @return 结果
     */
    @Override
    public int deleteOrgInfoByIds(String[] ids) {
        if(ids.length == 0){
            throw new ServiceException("请选择需要删除的数据！");
        }
        List<String> list = Arrays.asList(ids);
        return orgMapper.deleteOrgInfoByIds(list, SecurityUtils.getUserId());
    }

    /**
     * 数据导入
     * @param list
     * @param sb
     * @return
     */
    @Override
    public List<OrgInfo> importData(List<OrgParam> list, StringBuilder sb) {
        if(CollectionUtils.isEmpty(list)){
            throw new ServiceException("导入机构数据不能为空！");
        }
        List<OrgInfo> data = Lists.newArrayList();
        int num = 1;
        for (int i = 0; i < list.size(); i++) {
            OrgParam param = list.get(i);
            //校验excel输入
            judgeInput(num++, param, sb);
            OrgInfo info = new OrgInfo();
            BeanUtils.copyProperties(param, info);
            info.setId(IdUtils.simpleUUID());
            info.setOrgCode(CommonUtils.getRandom(6));
            info.setRoleType("0");
            info.setCreateTime(DateUtils.getNowDate());
            info.setDelFlag("0");
            info.setCreateBy(SecurityUtils.getUserId());
            data.add(info);
        }
        return data;
    }

    /**
     * 批量插入数据
     * @param data
     */
    @Override
    public void batchInsertData(List<OrgInfo> data) {
        if(CollectionUtils.isEmpty(data)){
            throw new ServiceException("导入机构数据不能为空！");
        }
        List<String> nameList = data.stream().map(OrgInfo::getOrgName).collect(Collectors.toList());
        List<OrgInfo> oldList = orgMapper.getListDataByName(nameList);
        if(CollectionUtils.isNotEmpty(oldList)){
            throw new ServiceException("存在导入机构数据，请勿重复导入！");
        }
        orgMapper.batchInsert(data);
    }

    /**
     * 校验excel输入
     * @param i
     * @param param
     * @param sb
     */
    private void judgeInput(int i, OrgParam param, StringBuilder sb) {
        if(StringUtils.isEmpty(param.getOrgName())){
            sb.append("第" + i + "行，机构名不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getOrgType())){
            sb.append("第" + i + "行，机构类型不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getLicenseNumber())){
            sb.append("第" + i + "行，营业执照号不能为空"+"<br>");
        }
        if(Objects.isNull(param.getLicenseTime())){
            sb.append("第" + i + "行，营业执照有效期不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getCompanyName())){
            sb.append("第" + i + "行，企业名称不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getCompanyPhone())){
            sb.append("第" + i + "行，公司电话不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getCompanyPhone(), RegexCommon.PHONE_COM_REGEX)){
                sb.append("第" + i + "行，公司电话格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getCompanyRePerson())){
            sb.append("第" + i + "行，公司法人不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getReIdcard())){
            sb.append("第" + i + "行，法人身份证号不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getReIdcard(), RegexCommon.ID_CARD_REGEX)){
                sb.append("第" + i + "行，法人身份证号格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getCreditCode())){
            sb.append("第" + i + "行，统一社会信用代码不能为空"+"<br>");
        }
        if(Objects.isNull(param.getRegisterAmount())){
            sb.append("第" + i + "行，注册资金不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getRegisterAmount().toString(), RegexCommon.NUM_REGEX)){
                sb.append("第" + i + "行，注册资金格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getPostCode())){
            sb.append("第" + i + "行，邮编不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getBusinessScope())){
            sb.append("第" + i + "行，经营范围不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getCompanyIntroduction())){
            sb.append("第" + i + "行，公司简介不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getInvoiceName())){
            sb.append("第" + i + "行，发票名称不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getBankName())){
            sb.append("第" + i + "行，开户行不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getBankNumber())){
            sb.append("第" + i + "行，开户账号不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getBankNumber().toString(), RegexCommon.BANK_NUM_REGEX)){
                sb.append("第" + i + "行，开户账号格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getContactName())){
            sb.append("第" + i + "行，联系人姓名不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(param.getContactPhone())){
            sb.append("第" + i + "行，手机号不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getContactPhone(), RegexCommon.PHONE_REGEX)){
                sb.append("第" + i + "行，手机号格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getRegisterAddress())){
            sb.append("第" + i + "行，注册地址不能为空"+"<br>");
        }
        if(Objects.isNull(param.getCompanyNum())){
            sb.append("第" + i + "行，人员规模不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeData(param.getCompanyNum().toString(), RegexCommon.NUM_REGEX)){
                sb.append("第" + i + "行，人员规模格式错误"+"<br>");
            }
        }
        if(StringUtils.isEmpty(param.getCompanyCode())){
            sb.append("第" + i + "行，商户编号不能为空"+"<br>");
        }
    }
}
