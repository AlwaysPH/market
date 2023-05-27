package com.market.activity.service.impl;

import com.google.common.collect.Lists;
import com.market.activity.mapper.*;
import com.market.activity.model.*;
import com.market.activity.service.ActivityService;
import com.market.common.core.domain.AjaxResult;
import com.market.common.enums.*;
import com.market.common.exception.ServiceException;
import com.market.common.utils.DateUtils;
import com.market.common.utils.RegexCommon;
import com.market.common.utils.SecurityUtils;
import com.market.common.utils.ip.IpUtils;
import com.market.common.utils.uuid.IdUtils;
import com.market.coupon.mapper.CouponMapper;
import com.market.coupon.mapper.CouponThresholdMapper;
import com.market.coupon.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-18 15:40
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ActivityServiceImpl implements ActivityService {

    private static final String SPLIT_STR = "~";

    @Autowired
    private ActivityMapper mapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ActivityCouponMapper activityCouponMapper;

    @Autowired
    private ActivitySpecialUserMapper specialUserMapper;

    @Autowired
    private GrantMapper grantMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private CouponThresholdMapper thresholdMapper;

    /**
     * 查询活动信息
     *
     * @param id 活动信息主键
     * @return 活动信息
     */
    @Override
    public ActivityInfo selectActivityInfoById(String id) {
        ActivityInfo info = mapper.selectActivityInfoById(id);
        //获取关联优惠券
        List<ActivityCouponInfo> list = activityCouponMapper.getCouponDataList(id);
        //获取已领取的优惠券
        List<UserCouponInfo> receiveList = couponMapper.getReceiveDataList(id, null);
        //获取优惠券发放数量
        List<GrantInfo> grantInfoList = grantMapper.getDataList(id, null);
        if(CollectionUtils.isNotEmpty(list)){
            //获取优惠券使用门槛
            List<String> idList = list.stream().map(ActivityCouponInfo::getCouponId).collect(Collectors.toList());
            List<CouponThreshold> thresholdList = thresholdMapper.getDataByCouponId(null, idList);
            /**关联优惠券列表**/
            buildContactCoupon(list, receiveList, grantInfoList, info, thresholdList);
            /**优惠券统计列表**/
            buildCouponTotal(list, receiveList, grantInfoList, info, thresholdList);
        }
        //关联商户
        List<MerchantInfo> merchantList = merchantMapper.getDataList(id);
        info.setMerchantList(merchantList);
        return info;
    }

    /**
     * 优惠券统计列表
     * @param list
     * @param receiveList
     * @param grantInfoList
     * @param info
     * @param thresholdList
     */
    private void buildCouponTotal(List<ActivityCouponInfo> list, List<UserCouponInfo> receiveList,
                                  List<GrantInfo> grantInfoList, ActivityInfo info, List<CouponThreshold> thresholdList) {
        Map<String, List<ActivityCouponInfo>> map = list.stream().collect(Collectors.groupingBy(ActivityCouponInfo::getCouponType));
        List<CouponTotal> data = Lists.newArrayList();
        map.forEach((key, tList) -> {
            CouponTotal total = new CouponTotal();
            total.setCouponTypeName(CouponTypeEnum.getNameByCode(key));
            total.setGrantNum(0);
            total.setGrantAmount(BigDecimal.ZERO);
            total.setReceiveNum(0);
            total.setReceiveAmount(BigDecimal.ZERO);
            total.setUseNum(0);
            total.setUseAmount(BigDecimal.ZERO);

            if(CollectionUtils.isNotEmpty(tList)){
                List<String> idList = tList.stream().map(ActivityCouponInfo::getCouponId).collect(Collectors.toList());
                //发放数量
                if(CollectionUtils.isNotEmpty(grantInfoList)){
                    List<GrantInfo> grantList = grantInfoList.stream().filter(e -> idList.contains(e.getCouponId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(grantList)){
                        int sum = grantList.stream().mapToInt(GrantInfo::getGrantNum).sum();
                        total.setGrantNum(sum);
                        List<String> collect = grantList.stream().map(GrantInfo::getCouponId).collect(Collectors.toList());
                        total.setGrantAmount(buildAmount(key, thresholdList, collect));
                    }
                }
                if(CollectionUtils.isNotEmpty(receiveList)){
                    //领取数量
                    List<UserCouponInfo> rList = receiveList.stream().filter(e -> idList.contains(e.getCouponId())).collect(Collectors.toList());
                    total.setReceiveNum(rList.size());
                    //使用数量
                    if(CollectionUtils.isNotEmpty(rList)){
                        List<String> containList = rList.stream().map(UserCouponInfo::getCouponId).collect(Collectors.toList());
                        total.setReceiveAmount(buildAmount(key, thresholdList, containList));
                        List<UserCouponInfo> collect = rList.stream().filter(e -> StringUtils.equalsIgnoreCase(e.getStatus(), CouponUseStatusEnum.USED.getCode())).collect(Collectors.toList());
                        total.setUseNum(collect.size());
                        total.setReceiveAmount(buildAmount(key, thresholdList, containList));
                    }
                }
            }
            data.add(total);
        });
        info.setTotalList(data);
    }

    /**
     * 获取总优惠
     * @param key
     * @param thresholdList
     * @param collect
     */
    private BigDecimal buildAmount(String key, List<CouponThreshold> thresholdList, List<String> collect) {
        if(CollectionUtils.isNotEmpty(thresholdList)){
            List<CouponThreshold> thresholds = thresholdList.stream().filter(e -> collect.contains(e.getCouponId())).collect(Collectors.toList());
            if(StringUtils.equalsIgnoreCase(key, CouponTypeEnum.FULL.getCode())){
                BigDecimal fullSub = thresholds.stream().map(e -> bigDecimalToZero(e.getFullSubAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal disFix = thresholds.stream().map(e -> bigDecimalToZero(e.getDisFixAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dis = thresholds.stream().map(e -> bigDecimalToZero(e.getDisAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
                return bigDecimalToZero(fullSub.add(disFix).add(dis));
            }else if(StringUtils.equalsIgnoreCase(key, CouponTypeEnum.VOUCHER.getCode())){
                BigDecimal face = thresholds.stream().map(e -> bigDecimalToZero(e.getFaceMoney())).reduce(BigDecimal.ZERO, BigDecimal::add);
                return bigDecimalToZero(face);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 关联优惠券列表
     * @param list
     * @param receiveList
     * @param grantInfoList
     * @param info
     * @param thresholdList
     */
    private void buildContactCoupon(List<ActivityCouponInfo> list, List<UserCouponInfo> receiveList,
                                    List<GrantInfo> grantInfoList, ActivityInfo info, List<CouponThreshold> thresholdList) {
        ActivityCouponInfo activityCouponInfo = list.get(0);
        info.setReceiveType(activityCouponInfo.getReceiveType());
        info.setEffectType(activityCouponInfo.getEffectType());
        info.setEffectStartTime(activityCouponInfo.getEffectStartTime());
        info.setEffectEndTime(activityCouponInfo.getEffectEndTime());
        info.setAlarmContent(activityCouponInfo.getAlarmContent());
        info.setAlarmNum(Objects.isNull(activityCouponInfo.getAlarmNum()) ? BigDecimal.ZERO : new BigDecimal(activityCouponInfo.getAlarmNum()));
        info.setAlarmPhone(activityCouponInfo.getAlarmPhone());
        info.setAlarmEmail(activityCouponInfo.getAlarmEmail());
        info.setDiffActivityFlag(activityCouponInfo.getDiffActivityFlag());
        info.setOutActivityFlag(activityCouponInfo.getOutActivityFlag());
        info.setSameActivityFlag(activityCouponInfo.getSameActivityFlag());
        //优惠券领取
        Map<String, List<UserCouponInfo>> map = null;
        if(CollectionUtils.isNotEmpty(receiveList)){
            map = receiveList.stream().collect(Collectors.groupingBy(UserCouponInfo::getCouponId));
        }
        //优惠券发放
        Map<String, List<GrantInfo>> grantMap = null;
        if(CollectionUtils.isNotEmpty(grantInfoList)){
            grantMap = grantInfoList.stream().collect(Collectors.groupingBy(GrantInfo::getCouponId));
        }
        //优惠券使用门槛
        Map<String, List<CouponThreshold>> thresholdMap = null;
        if(CollectionUtils.isNotEmpty(thresholdList)){
            thresholdMap = thresholdList.stream().collect(Collectors.groupingBy(CouponThreshold::getCouponId));
        }
        Map<String, List<UserCouponInfo>> finalMap = map;
        Map<String, List<GrantInfo>> finalGrantMap = grantMap;
        Map<String, List<CouponThreshold>> finalThresholdMap = thresholdMap;
        list.forEach(e -> {
            //领取数量
            if(Objects.nonNull(finalMap)){
                List<UserCouponInfo> infoList = finalMap.get(e.getCouponId());
                e.setReceiveNum(CollectionUtils.isEmpty(infoList) ? 0 : infoList.size());
            }
            //发放数量
            if(Objects.nonNull(finalGrantMap)){
                List<GrantInfo> infoList = finalGrantMap.get(e.getCouponId());
                if(CollectionUtils.isNotEmpty(infoList)){
                    int sum = infoList.stream().mapToInt(GrantInfo::getGrantNum).sum();
                    e.setGrantNum(sum);
                }
            }
            //优惠券使用门槛
            if(Objects.nonNull(finalThresholdMap)){
                List<CouponThreshold> thresholds = finalThresholdMap.get(e.getCouponId());
                if(CollectionUtils.isNotEmpty(thresholds)){
                    CouponThreshold threshold = thresholds.get(0);
                    if(StringUtils.equalsIgnoreCase(e.getCouponType(), CouponTypeEnum.FULL.getCode())){
                        /**满减券**/
                        if(StringUtils.equalsIgnoreCase(threshold.getRestrictionType(), RestrictionTypeEnum.NO.getCode())){
                            //无限制
                            e.setCouponAmount(bigDecimalToZero(threshold.getDisAmount()));
                        }else if(StringUtils.equalsIgnoreCase(threshold.getRestrictionType(), RestrictionTypeEnum.FIX.getCode())){
                            //固定满减
                            e.setCouponAmount(bigDecimalToZero(threshold.getDisFixAmount()));
                        }else if(StringUtils.equalsIgnoreCase(threshold.getRestrictionType(), RestrictionTypeEnum.LADDER.getCode())){
                            //阶梯满减
                            e.setCouponAmount(bigDecimalToZero(threshold.getFullSubAmount()));
                        }
                    }else if(StringUtils.equalsIgnoreCase(e.getCouponType(), CouponTypeEnum.SALE.getCode())){
                        /**折扣券**/
                        BigDecimal sum = threshold.getFullRestriction().multiply(threshold.getSaleNum()).divide(new BigDecimal(100));
                        e.setCouponAmount(bigDecimalToZero(sum));
                        if(StringUtils.equalsIgnoreCase(threshold.getUpperLimitFlag(), UpperLimitTypeEnum.YES.getCode())){
                            if(sum.compareTo(threshold.getUpperLimitAmount()) > 0){
                                e.setCouponAmount(bigDecimalToZero(threshold.getUpperLimitAmount()));
                            }
                        }
                    }else if(StringUtils.equalsIgnoreCase(e.getCouponType(), CouponTypeEnum.VOUCHER.getCode())){
                        /**代金券**/
                        e.setCouponAmount(bigDecimalToZero(threshold.getFaceMoney()));
                    }
                }
            }
            //判断券可用状态和失效时间
            if(StringUtils.equalsIgnoreCase(e.getEffectType(), CouponEffectTypeEnum.FIX.getCode())){
                if(e.getEffectEndTime().getTime() >= info.getEndTime().getTime()){
                    e.setAvailableStatus(AvailableStatusEnum.NO_AVAILABLE.getCode());
                }else {
                    e.setAvailableStatus(AvailableStatusEnum.AVAILABLE.getCode());
                }
            }else if(StringUtils.equalsIgnoreCase(e.getEffectType(), CouponEffectTypeEnum.ACC.getCode())){
                //配置日期，领取日期+有效天数
                if(Objects.nonNull(finalMap)){
                    List<UserCouponInfo> infoList = finalMap.get(e.getCouponId());
                    if(CollectionUtils.isNotEmpty(infoList)){
                        UserCouponInfo couponInfo = infoList.get(0);
                        Date receiveTime = couponInfo.getReceiveTime();
                        if(DateUtils.judgeTime(receiveTime, e.getEffectDateNum())){
                            e.setAvailableStatus(AvailableStatusEnum.AVAILABLE.getCode());
                        }else {
                            e.setAvailableStatus(AvailableStatusEnum.AVAILABLE.getCode());
                        }
                    }
                }
            }
        });
        info.setContactList(list);
    }

    private BigDecimal bigDecimalToZero(BigDecimal amount) {
        return Objects.isNull(amount) ? BigDecimal.ZERO : amount;
    }

    /**
     * 查询活动信息列表
     *
     * @param info 活动信息
     * @return 活动信息
     */
    @Override
    public List<ActivityInfo> selectActivityInfoList(ActivityInfo info) {
        List<ActivityInfo> list = mapper.selectActivityInfoList(info);
        if(CollectionUtils.isEmpty(list)){
            return Lists.newArrayList();
        }
        List<String> idList = list.stream().map(ActivityInfo::getId).collect(Collectors.toList());
        //获取活动关联优惠券
        List<ActivityCouponInfo> couponInfoList = activityCouponMapper.getDataList(null, null, idList);
        if(CollectionUtils.isEmpty(couponInfoList)){
            return list;
        }
        Map<String, List<ActivityCouponInfo>> collect = couponInfoList.stream().collect(Collectors.groupingBy(ActivityCouponInfo::getActivityId));
        list.forEach(e -> {
            List<ActivityCouponInfo> infoList = collect.get(e.getId());
            e.setCouponNum(CollectionUtils.isEmpty(infoList) ? 0 : infoList.size());
            if(CollectionUtils.isNotEmpty(infoList)){
                ActivityCouponInfo couponInfo = infoList.get(0);
                e.setReceiveType(Objects.isNull(couponInfo) ? null : couponInfo.getReceiveType());
            }
        });
        //获取活动关联商户
        return list;
    }

    /**
     * 新增活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    @Override
    public int insertActivityInfo(ActivityInfo info) {
        info.setCreateBy(SecurityUtils.getUserId());
        info.setCreateTime(DateUtils.getNowDate());
        info.setDelFlag("0");
        info.setId(IdUtils.simpleUUID());
        info.setStatus(ActivityStatusEnum.NO_START.getCode());
        info.setApproveStatus(AuditStatusEnum.TO_BE_SUBMIT.getCode());
        info.setOperationType(OperationTypeEnum.INSERT.getCode());
        return mapper.insertActivityInfo(info);
    }

    /**
     * 修改活动信息
     *
     * @param info 活动信息
     * @return 结果
     */
    @Override
    public int updateActivityInfo(ActivityInfo info) {

        ActivityInfo activityInfo = mapper.selectActivityInfoById(info.getId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("活动信息不存在");
        }
        if(StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.REJECT.getCode())
            || StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.TO_BE_SUBMIT.getCode())){
            throw new ServiceException("不允许修改审核中或审核完成的数据");
        }
        if(StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.REJECT.getCode())){
            activityInfo.setOperationType(OperationTypeEnum.UPDATE.getCode());
        }
        activityInfo.setUpdateTime(DateUtils.getNowDate());
        activityInfo.setUpdateBy(SecurityUtils.getUserId());
        return mapper.updateActivityInfo(activityInfo);
    }

    /**
     * 批量删除活动信息
     *
     * @param ids 需要删除的活动信息主键
     * @return 结果
     */
    @Override
    public int deleteActivityInfoByIds(String[] ids) {
        if(ids.length == 0){
            throw new ServiceException("请选择需要删除的数据！");
        }
        List<String> list = Arrays.asList(ids);
        String userId = SecurityUtils.getUserId();
        //删除活动信息
        int i = mapper.deleteActivityInfoByIds(list, userId);
        //删除活动关联优惠券信息
        i += activityCouponMapper.delete(list, userId);
        //删除活动关联商户信息
        i += merchantMapper.delete(list, userId);
        //删除活动指定用户信息
        i += specialUserMapper.delete(list, userId);
        return i;
    }

    /**
     * 查询优惠券关联活动列表
     * @param info
     * @return
     */
    @Override
    public List<ActivityInfo> getListByCoupon(ActivityInfo info) {
        if(StringUtils.isEmpty(info.getCouponId())){
            throw new ServiceException("参数不能为空");
        }
        CouponInfo couponInfo = couponMapper.selectCouponInfoById(info.getCouponId());
        if(Objects.isNull(couponInfo)){
            throw new ServiceException("优惠券信息不存在");
        }
        return mapper.getListByCoupon(info);
    }

    /**
     * 审核活动信息
     * @param info
     * @return
     */
    @Override
    public int audit(ActivityInfo info) {
        if(StringUtils.isEmpty(info.getId())){
            throw new ServiceException("活动id不能为空");
        }
        if(StringUtils.isEmpty(info.getApproveStatus())){
            throw new ServiceException("审核结果不能为空");
        }
        if(StringUtils.isEmpty(info.getAuditRemark())){
            throw new ServiceException("审核备注不能为空");
        }
        ActivityInfo data = mapper.selectActivityInfoById(info.getId());
        if(Objects.isNull(data)){
            throw new ServiceException("未找到活动信息");
        }
        if(!StringUtils.equalsIgnoreCase(data.getApproveStatus(), AuditStatusEnum.REVIEWED.getCode())){
            throw new ServiceException("只允许操作待审核的数据！");
        }
        data.setApproveStatus(info.getApproveStatus());
        data.setAuditRemark(info.getAuditRemark());
        data.setAuditTime(new Date());
        data.setUpdateBy(SecurityUtils.getUserId());
        data.setUpdateTime(new Date());
        //判断当前审核时间是否在活动时间之内
        if(System.currentTimeMillis() >= data.getStartTime().getTime()
            && System.currentTimeMillis() < data.getEndTime().getTime()){
            data.setStatus(ActivityStatusEnum.ING.getCode());
        }else if(System.currentTimeMillis() < data.getStartTime().getTime()){
            data.setStatus(ActivityStatusEnum.NO_START.getCode());
        }else if(System.currentTimeMillis() > data.getEndTime().getTime()){
            data.setStatus(ActivityStatusEnum.FAILURE.getCode());
        }
        return mapper.updateActivityInfo(data);
    }

    /**
     * 提交审核
     * @param info
     * @return
     */
    @Override
    public int submitAudit(ActivityInfo info) {
        if(StringUtils.isEmpty(info.getId())){
            throw new ServiceException("活动id不能为空");
        }
        ActivityInfo data = mapper.selectActivityInfoById(info.getId());
        if(Objects.isNull(data)){
            throw new ServiceException("未找到活动信息");
        }
        data.setApproveStatus(AuditStatusEnum.REVIEWED.getCode());
        data.setUpdateTime(new Date());
        data.setUpdateBy(SecurityUtils.getUserId());
        return mapper.updateActivityInfo(data);
    }

    /**
     * 配置优惠券
     * @param params
     * @return
     */
    @Override
    public int submitCoupon(ActivityCouponParams params) {
        ActivityInfo info = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(info)){
            throw new ServiceException("不存在当前活动信息!");
        }
        if(StringUtils.equalsIgnoreCase(params.getEffectType(), CouponEffectTypeEnum.FIX.getCode())){
            if(Objects.isNull(params.getEffectStartTime())){
                throw new ServiceException("券使用有效期开始时间不能为空!");
            }
            if(Objects.isNull(params.getEffectEndTime())){
                throw new ServiceException("券使用有效期结束时间不能为空!");
            }
            if(params.getEffectStartTime().getTime() < info.getStartTime().getTime()){
                throw new ServiceException("券使用有效期开始时间不能小于活动开始时间!");
            }
            if(params.getEffectStartTime().getTime() > info.getEndTime().getTime()){
                throw new ServiceException("券使用有效期开始时间不能大于活动结束时间!");
            }
            if(params.getEffectEndTime().getTime() > info.getEndTime().getTime()){
                throw new ServiceException("券使用有效期结束时间不能大于活动结束时间!");
            }
            if(params.getEffectEndTime().getTime() < info.getStartTime().getTime()){
                throw new ServiceException("券使用有效期结束时间不能小于活动开始时间!");
            }
        }else if(StringUtils.equalsIgnoreCase(params.getEffectType(), CouponEffectTypeEnum.ACC.getCode())){
            if(Objects.isNull(params.getEffectDateNum())){
                throw new ServiceException("券有效天数不能为空!");
            }
            if(Objects.isNull(params.getTakeDateNum())){
                throw new ServiceException("券生效天数不能为空!");
            }
        }
        List<ActivityCouponInfo> list = params.getCouponInfoList();
        //查询当前活动已配置的优惠券
        List<ActivityCouponInfo> data = activityCouponMapper.getDataList(params.getActivityId(), null, null);
        if(CollectionUtils.isNotEmpty(data)){
            List<String> collect = data.stream().map(ActivityCouponInfo::getCouponId).collect(Collectors.toList());
            List<ActivityCouponInfo> containList = list.stream().filter(e -> collect.contains(e.getCouponId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(containList)){
                throw new ServiceException("当前活动不允许重复配置相同优惠券");
            }
        }
        list.forEach(e -> {
            e.setCouponId(e.getId());
            e.setId(IdUtils.simpleUUID());
            e.setActivityId(params.getActivityId());
            e.setInventoryNum(e.getInventoryNum());
            e.setGrantNum(e.getNowGrantNum());
            e.setEffectType(params.getEffectType());
            e.setEffectStartTime(params.getEffectStartTime());
            e.setEffectEndTime(params.getEffectEndTime());
            e.setTakeDateNum(params.getTakeDateNum());
            e.setEffectDateNum(params.getEffectDateNum());
            e.setReceiveType(params.getReceiveType());
            e.setSameActivityFlag(params.getSameActivityFlag());
            e.setDiffActivityFlag(params.getDiffActivityFlag());
            e.setOutActivityFlag(params.getOutActivityFlag());
            e.setAlarmNum(params.getAlarmNum());
            e.setAlarmContent(params.getAlarmContent());
            e.setAlarmPhone(params.getAlarmPhone());
            e.setAlarmEmail(params.getAlarmEmail());
            e.setDelFlag("0");
            e.setCreateBy(SecurityUtils.getUserId());
            e.setCreateTime(new Date());
        });
        //活动关联优惠券入库
        return activityCouponMapper.insertData(list);
    }

    /**
     * 指定用户
     * @param params
     * @return
     */
    @Override
    public int specifyUser(ActivityCouponParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动id不能为空");
        }
        List<CardInfo> infoList = params.getCardInfoList();
        if(CollectionUtils.isEmpty(infoList)){
            throw new ServiceException("指定用户不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        if(StringUtils.equalsIgnoreCase(activityInfo.getReceiveType(), ReceiveTypeEnum.SPECIFY_USER.getCode())){
            throw new ServiceException("当前活动信息领取门槛信息不匹配");
        }
        infoList.forEach(e -> {
            e.setId(IdUtils.simpleUUID());
            e.setActivityId(params.getActivityId());
            e.setCreateBy(SecurityUtils.getUserId());
            e.setCreateTime(new Date());
            e.setDelFlag("0");
        });
        return specialUserMapper.insertData(infoList);
    }

    /**
     * 获取活动关联优惠券信息
     * @param activityId
     * @return
     */
    @Override
    public List<GrantInfo> getGrantCoupon(String activityId) {
        if(StringUtils.isEmpty(activityId)){
            throw new ServiceException("活动id不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(activityId);
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        return activityCouponMapper.getCouponDetailList(activityId);
    }

    /**
     * 发放优惠券
     * @param info
     * @return
     */
    @Override
    public int grantCoupon(GrantInfo info) {
        if(StringUtils.isEmpty(info.getActivityId())){
            throw new ServiceException("活动id不能为空");
        }
        if(StringUtils.isEmpty(info.getCouponId())){
            throw new ServiceException("优惠券id不能为空");
        }
        if(Objects.isNull(info.getGrantNum())){
            throw new ServiceException("发放数量不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(info.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.PASS.getCode())){
            throw new ServiceException("当前活动未审核通过，不允许操作");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getStatus(), ActivityStatusEnum.ING.getCode())){
            throw new ServiceException("当前活动未在进行中，不允许操作");
        }
        CouponInfo couponInfo = couponMapper.selectCouponInfoById(info.getCouponId());
        if(Objects.isNull(couponInfo)){
            throw new ServiceException("当前优惠券不存在");
        }
        List<ActivityCouponInfo> dataList = activityCouponMapper.getDataList(info.getActivityId(), info.getCouponId(), null);
        if(CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("当前活动未关联改优惠券");
        }
        ActivityCouponInfo activityCouponInfo = dataList.get(0);
        if(activityCouponInfo.getInventoryNum() < info.getGrantNum()){
            throw new ServiceException("库存不足");
        }
        info.setId(IdUtils.simpleUUID());
        info.setCreateBy(SecurityUtils.getUserId());
        info.setCreateTime(new Date());
        //修改优惠券库存
        activityCouponInfo.setInventoryNum(activityCouponInfo.getInventoryNum() - info.getGrantNum());
        activityCouponInfo.setUpdateBy(SecurityUtils.getUserId());
        activityCouponInfo.setUpdateTime(new Date());
        activityCouponMapper.update(activityCouponInfo);
        //执行券发放任务(自动发放)
        if(StringUtils.equalsIgnoreCase(couponInfo.getSendType(), CouponSendTypeEnum.AUTOMATIC.getCode())){
            executeGrantCouponJob(activityInfo, info, couponInfo);
        }
        return grantMapper.insert(info);
    }

    /**
     * 执行券发放任务
     * @param activityInfo
     * @param info
     * @param couponInfo
     */
    private void executeGrantCouponJob(ActivityInfo activityInfo, GrantInfo info, CouponInfo couponInfo) {

    }

    /**
     * 卡分类数据导入
     * @param list
     * @param sb
     * @param activityId
     * @return
     */
    @Override
    public List<CardInfo> importData(List<CardInfo> list, StringBuilder sb, String activityId) {
        if(CollectionUtils.isEmpty(list)){
            throw new ServiceException("卡分类数据不能为空！");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(activityId);
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        int num = 1;
        for (int i = 0; i < list.size(); i++) {
            CardInfo info = list.get(i);
            info.setId(IdUtils.simpleUUID());
            info.setDelFlag("0");
            info.setActivityId(activityId);
            info.setCreateBy(SecurityUtils.getUserId());
            info.setCreateTime(new Date());
            //校验excel输入
            judgeInput(num++, info, sb);
        }
        return list;
    }

    /**
     * 卡分类数据入库
     * @param data
     */
    @Override
    public void batchInsertCardData(List<CardInfo> data) {
        if(CollectionUtils.isEmpty(data)){
            throw new ServiceException("卡分类数据不能为空！");
        }
        specialUserMapper.insertData(data);
    }

    /**
     * 配置商户
     * @param params
     * @return
     */
    @Override
    public int configMerchant(ActivityCouponParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动id不能为空");
        }
        List<MerchantInfo> list = params.getMerchantList();
        if(CollectionUtils.isEmpty(list)){
            throw new ServiceException("商户信息不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        list.forEach(e -> {
            e.setId(IdUtils.simpleUUID());
            e.setActivityId(params.getActivityId());
            e.setCreateBy(SecurityUtils.getUserId());
            e.setCreateTime(new Date());
            e.setDelFlag("0");
        });
        return merchantMapper.insertData(list);
    }

    /**
     * 商户数据导入
     * @param list
     * @param sb
     * @param activityId
     * @return
     */
    @Override
    public List<MerchantInfo> importMerchantData(List<MerchantInfo> list, StringBuilder sb, String activityId) {
        if(CollectionUtils.isEmpty(list)){
            throw new ServiceException("商户数据不能为空！");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(activityId);
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在");
        }
        int num = 1;
        for (int i = 0; i < list.size(); i++) {
            MerchantInfo info = list.get(i);
            info.setId(IdUtils.simpleUUID());
            info.setDelFlag("0");
            info.setActivityId(activityId);
            info.setCreateBy(SecurityUtils.getUserId());
            info.setCreateTime(new Date());
            info.setEffectStartTime(activityInfo.getStartTime());
            info.setEffectEndTime(activityInfo.getEndTime());
            //校验excel输入
            judgeMerchantInput(num++, info, sb);
        }
        return list;
    }

    /**
     * 商户数据入库
     * @param data
     */
    @Override
    public void batchInsertMerchantData(List<MerchantInfo> data) {
        if(CollectionUtils.isEmpty(data)){
            throw new ServiceException("商户数据不能为空！");
        }
        merchantMapper.insertData(data);
    }

    /**
     * 停止活动
     * @param params
     * @return
     */
    @Override
    public int stop(ActivityCouponParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动id不能为空！");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在！");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.PASS.getCode())){
            throw new ServiceException("当前活动未审核通过，不允许操作！");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getStatus(), ActivityStatusEnum.ING.getCode())){
            throw new ServiceException("当前活动未在进行中，不允许操作！");
        }
        activityInfo.setStatus(ActivityStatusEnum.STOP.getCode());
        activityInfo.setUpdateBy(SecurityUtils.getUserId());
        activityInfo.setUpdateTime(new Date());
        return mapper.updateActivityInfo(activityInfo);
    }

    /**
     * 重启活动
     * @param params
     * @return
     */
    @Override
    public int restart(ActivityCouponParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动id不能为空！");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动信息不存在！");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.PASS.getCode())){
            throw new ServiceException("当前活动未审核通过，不允许操作！");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getStatus(), ActivityStatusEnum.STOP.getCode())){
            throw new ServiceException("当前活动未在停止状态，不允许操作！");
        }
        activityInfo.setStatus(ActivityStatusEnum.ING.getCode());
        activityInfo.setUpdateBy(SecurityUtils.getUserId());
        activityInfo.setUpdateTime(new Date());
        return mapper.updateActivityInfo(activityInfo);
    }

    /**
     * 获取APP的活动列表
     * @return
     * @param params
     */
    @Override
    public List<ActivityInfo> getAppActivityList(AppParams params) {
        if(Objects.isNull(params.getUserType())){
            throw new ServiceException("用户类型不能为空");
        }
        List<String> typeList = Arrays.asList(params.getUserType().toString());
        if(StringUtils.equalsIgnoreCase(params.getUserType().toString(), ReceiveTypeEnum.NEW_USER.getCode())
                ||  StringUtils.equalsIgnoreCase(params.getUserType().toString(), ReceiveTypeEnum.NEW_ORDER.getCode())){
            if(StringUtils.isEmpty(params.getPhoneNumber())){
                throw new ServiceException("用户手机号不能为空");
            }
            typeList.add(ReceiveTypeEnum.ALL.getCode());
        }
        //获取全部、新用户或全部、首单的活动数据
        List<ActivityInfo> list = mapper.getAppActivityList(typeList);
        //获取当前账号指定参加的活动数据
        if(StringUtils.isNotEmpty(params.getPhoneNumber())){
            List<ActivityInfo> specialList = specialUserMapper.getSpecialDataByPhone(params.getPhoneNumber());
            list.addAll(specialList);
        }
        return list;
    }

    /**
     * 获取APP的活动关联优惠券列表
     * @param info
     * @return
     */
    @Override
    public List<CouponThreshold> getAppActivityCouponList(ActivityInfo info) {
        if(StringUtils.isEmpty(info.getId())){
            throw new ServiceException("参数不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(info.getId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动不存在");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.PASS.getCode())){
            throw new ServiceException("当前活动未审核通过");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getStatus(), ActivityStatusEnum.ING.getCode())){
            throw new ServiceException("当前活动未在进行中");
        }
        if(activityInfo.getStartTime().getTime() > System.currentTimeMillis()){
            throw new ServiceException("当前活动未开始");
        }
        if(activityInfo.getEndTime().getTime() < System.currentTimeMillis()){
            throw new ServiceException("当前活动已结束");
        }
        List<CouponThreshold> list = thresholdMapper.getAppActivityCouponList(info);
        List<CouponThreshold> result = Lists.newArrayList();
        //阶梯满减券使用门槛合并
        if(CollectionUtils.isNotEmpty(list)){
            //阶梯满减券
            List<CouponThreshold> collect = list.stream().filter(e -> StringUtils.equalsIgnoreCase(e.getRestrictionType(), RestrictionTypeEnum.LADDER.getCode())).collect(Collectors.toList());
            //非阶梯满减券
            List<CouponThreshold> otherList = list.stream().filter(e -> !StringUtils.equalsIgnoreCase(e.getRestrictionType(), RestrictionTypeEnum.LADDER.getCode())).collect(Collectors.toList());
            result.addAll(otherList);
            if(CollectionUtils.isNotEmpty(collect)){
                CouponThreshold threshold = collect.get(0);
                threshold.setLadderList(collect);
                result.add(threshold);
            }
        }
        //获取优惠券发放
        List<GrantInfo> grantList = grantMapper.getDataList(info.getId(), null);
        Map<String, List<GrantInfo>> grantMap = null;
        if(CollectionUtils.isNotEmpty(grantList)){
            grantMap = grantList.stream().collect(Collectors.groupingBy(GrantInfo::getCouponId));
        }
        //优惠券领取
        List<UserCouponInfo> userList = couponMapper.getReceiveList(null, info.getId());
        Map<String, List<UserCouponInfo>> userMap = null;
        if(CollectionUtils.isNotEmpty(userList)){
            userMap = userList.stream().collect(Collectors.groupingBy(UserCouponInfo::getCouponId));
        }
        Map<String, List<GrantInfo>> finalGrantMap = grantMap;
        Map<String, List<UserCouponInfo>> finalUserMap = userMap;
        result.forEach(e -> {
            int grantNum = 0;
            int receiveNum = 0;
            if(Objects.nonNull(finalGrantMap)){
                List<GrantInfo> grantInfos = finalGrantMap.get(e.getCouponId());
                if(CollectionUtils.isNotEmpty(grantInfos)){
                    grantNum = grantInfos.stream().mapToInt(GrantInfo::getGrantNum).sum();
                    e.setGrantNum(grantNum);
                }
            }
            if(Objects.nonNull(finalUserMap)){
                List<UserCouponInfo> couponInfos = finalUserMap.get(e.getCouponId());
                if(CollectionUtils.isNotEmpty(couponInfos)){
                    receiveNum = couponInfos.stream().mapToInt(UserCouponInfo::getReceiveNum).sum();
                    e.setReceiveNum(receiveNum);
                }
            }
            int surplusNum = grantNum - receiveNum;
            e.setSurplusNum(surplusNum > 0 ? surplusNum : 0);
            e.setFullFlag(receiveNum >= grantNum ? 1 : 0);
        });
        return result;
    }

    /**
     * 领取优惠券
     * @param params
     * @return
     */
    @Override
    public AjaxResult receiveCoupon(AppParams params) {
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        checkActivity(activityInfo);
        CouponInfo couponInfo = couponMapper.selectCouponInfoById(params.getCouponId());
        if(Objects.isNull(couponInfo)){
            throw new ServiceException("当前优惠券不存在");
        }
        params.setSendType(couponInfo.getSendType());
        String deviceNo = params.getDeviceNo();
        //每人限领
        if(Objects.nonNull(couponInfo.getPersonLimit())){
            params.setDeviceNo(null);
            int num = couponMapper.getPersonReceiveNum(params);
            if(num >= couponInfo.getPersonLimit()){
                throw new ServiceException("当前优惠券已领取，请勿重新领取");
            }
        }
        //每日限领
        if(Objects.nonNull(couponInfo.getDateLimit())){
            params.setReceiveTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date()));
            int num = couponMapper.getPersonReceiveNum(params);
            if(num >= couponInfo.getDateLimit()){
                throw new ServiceException("当前优惠券已领取，请勿重新领取");
            }
        }
        //每台每日限领
        if(Objects.nonNull(couponInfo.getMachineLimit())){
            params.setDeviceNo(deviceNo);
            int num = couponMapper.getPersonReceiveNum(params);
            if(num >= couponInfo.getMachineLimit()){
                throw new ServiceException("当前优惠券已领取，请勿重新领取");
            }
        }
        List<ActivityCouponInfo> dataList = activityCouponMapper.getDataList(params.getActivityId(), params.getCouponId(), null);
        if(CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("当前优惠券不属于当前活动，操作失败");
        }
        ActivityCouponInfo info = dataList.get(0);
        //领取门槛为指定用户
        if(StringUtils.equalsIgnoreCase(info.getReceiveType(), ReceiveTypeEnum.SPECIFY_USER.getCode())){
            int num = specialUserMapper.getSpecialData(params);
            if(num <= 0){
                throw new ServiceException("当前用户不在活动范围内，操作失败");
            }
        }
        //判断优惠券库存
        List<GrantInfo> grantInfos = grantMapper.getDataList(params.getActivityId(), params.getCouponId());
        if(CollectionUtils.isNotEmpty(grantInfos)){
            GrantInfo grantInfo = grantInfos.get(0);
            AppParams param = new AppParams();
            param.setCouponId(params.getCouponId());
            param.setActivityId(params.getActivityId());
            int num = couponMapper.getPersonReceiveNum(param);
            if(num >= grantInfo.getGrantNum().intValue()){
                throw new ServiceException("当前优惠券库存不足，操作失败");
            }
        }
        Date date = new Date();
        UserCouponInfo data = new UserCouponInfo();
        BeanUtils.copyProperties(params, data);
        data.setId(IdUtils.simpleUUID());
        data.setIpAddress(IpUtils.getIpAddr());
        data.setReceiveTime(date);
        data.setStatus(judgeCouponStatus(activityInfo, info, date));
        data.setChannelType(couponInfo.getChannelType());
        couponMapper.insertCouponUser(data);
        return AjaxResult.success();
    }

    /**
     * 判断用户领取优惠券使用状态
     * @param activityInfo
     * @param info
     * @param date
     * @return
     */
    private String judgeCouponStatus(ActivityInfo activityInfo, ActivityCouponInfo info,
                                     Date date) {
        if(System.currentTimeMillis() >= activityInfo.getStartTime().getTime()
            && System.currentTimeMillis() < activityInfo.getEndTime().getTime()){
            if (com.market.common.utils.StringUtils.equalsIgnoreCase(info.getEffectType(), CouponEffectTypeEnum.FIX.getCode())) {
                /**固定日期**/
                if(System.currentTimeMillis() >= info.getEffectStartTime().getTime()
                    && System.currentTimeMillis() < info.getEffectEndTime().getTime()){
                    return CouponUseStatusEnum.UN_USED.getCode();
                }else {
                    return CouponUseStatusEnum.TIME_OUT.getCode();
                }
            }else if(com.market.common.utils.StringUtils.equalsIgnoreCase(info.getEffectType(), CouponEffectTypeEnum.ACC.getCode())){
                /**累计日期**/
                Date afterDate = DateUtils.daysAgoOrAfterToDate(date, info.getEffectDateNum() + info.getTakeDateNum());
                if(System.currentTimeMillis() < afterDate.getTime()){
                    return CouponUseStatusEnum.UN_USED.getCode();
                }else {
                    return CouponUseStatusEnum.TIME_OUT.getCode();
                }
            }
        }else if(System.currentTimeMillis() >= activityInfo.getEndTime().getTime()){
            return CouponUseStatusEnum.TIME_OUT.getCode();
        }else if(System.currentTimeMillis() < activityInfo.getStartTime().getTime()){
            return CouponUseStatusEnum.UN_EFFECT.getCode();
        }
        return CouponUseStatusEnum.UN_USED.getCode();
    }

    /**
     * 获取APP优惠券使用门店
     * @param params
     * @return
     */
    @Override
    public List<MerchantInfo> getAPPCouponMerchant(AppParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动ID参数不能为空");
        }
        if(StringUtils.isEmpty(params.getCouponId())){
            throw new ServiceException("优惠券ID参数不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        checkActivity(activityInfo);
        CouponInfo couponInfo = couponMapper.selectCouponInfoById(params.getCouponId());
        if(Objects.isNull(couponInfo)){
            throw new ServiceException("当前优惠券不存在");
        }
        return merchantMapper.getMerchantList(params.getActivityId());
    }

    /**
     * 获取活动详情
     * @param params
     * @return
     */
    @Override
    public AjaxResult getAPPActivityDetail(AppParams params) {
        if(StringUtils.isEmpty(params.getActivityId())){
            throw new ServiceException("活动ID参数不能为空");
        }
        ActivityInfo activityInfo = mapper.selectActivityInfoById(params.getActivityId());
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动不存在");
        }
        return AjaxResult.success(activityInfo);
    }

    /**
     * 活动校验
     * @param activityInfo
     */
    private void checkActivity( ActivityInfo activityInfo){
        if(Objects.isNull(activityInfo)){
            throw new ServiceException("当前活动不存在");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getApproveStatus(), AuditStatusEnum.PASS.getCode())){
            throw new ServiceException("当前活动未审核通过");
        }
        if(!StringUtils.equalsIgnoreCase(activityInfo.getStatus(), ActivityStatusEnum.ING.getCode())){
            throw new ServiceException("当前活动未在进行中");
        }
        if(activityInfo.getStartTime().getTime() > System.currentTimeMillis()){
            throw new ServiceException("当前活动未开始");
        }
        if(activityInfo.getEndTime().getTime() < System.currentTimeMillis()){
            throw new ServiceException("当前活动已结束");
        }
    }

    /**
     * 商户数据导入校验
     * @param i
     * @param info
     * @param sb
     */
    private void judgeMerchantInput(int i, MerchantInfo info, StringBuilder sb) {
        if(StringUtils.isEmpty(info.getMerchantNo())){
            sb.append("第" + i + "行，商户编号不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getMerchantName())){
            sb.append("第" + i + "行，商户名称不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getIndustryType())){
            sb.append("第" + i + "行，行业类型不能为空"+"<br>");
        }else {
            IndustryTypeEnum industryTypeEnum = IndustryTypeEnum.getEnumByCode(info.getIndustryType());
            if(Objects.isNull(industryTypeEnum)){
                sb.append("第" + i + "行，行业类型不存在"+"<br>");
            }
        }
        if(StringUtils.isEmpty(info.getSettleType())){
            sb.append("第" + i + "行，活动营销资金结算方式不能为空"+"<br>");
        }else {
            SettleTypeEnum settleTypeEnum = SettleTypeEnum.getEnumByCode(info.getSettleType());
            if(Objects.isNull(settleTypeEnum)){
                sb.append("第" + i + "行，活动营销资金结算方式不存在"+"<br>");
            }
        }
        if(Objects.isNull(info.getConsumRate())){
            sb.append("第" + i + "行，商户原消费费率不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeNum(new BigDecimal(info.getConsumRate()), RegexCommon.TWO_REGEX)
                || info.getConsumRate() < 0 || info.getConsumRate() > 100){
                sb.append("第" + i + "行，商户原消费费率输入数字不正确"+"<br>");
            }
        }
        if(Objects.isNull(info.getActivityConsumRate())){
            sb.append("第" + i + "行，活动期间商户消费费率不能为空"+"<br>");
        }else {
            if(!RegexCommon.judgeNum(new BigDecimal(info.getActivityConsumRate()), RegexCommon.TWO_REGEX)
                    || info.getConsumRate() < 0 || info.getConsumRate() > 100){
                sb.append("第" + i + "行，活动期间商户消费费率输入数字不正确"+"<br>");
            }
        }
    }

    /**
     * 卡分类数据导入校验
     * @param i
     * @param info
     * @param sb
     */
    private void judgeInput(int i, CardInfo info, StringBuilder sb) {
        if(StringUtils.isEmpty(info.getCardNo())){
            sb.append("第" + i + "行，卡属性编号不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getCardName())){
            sb.append("第" + i + "行，卡属性名称不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getAppUserId())){
            sb.append("第" + i + "行，APPuserID不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getAppAccount())){
            sb.append("第" + i + "行，APP账号不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getCompanyUserName())){
            sb.append("第" + i + "行，企业客户名称不能为空"+"<br>");
        }
        if(StringUtils.isEmpty(info.getCardFaceName())){
            sb.append("第" + i + "行，卡面名称不能为空"+"<br>");
        }
    }

}
