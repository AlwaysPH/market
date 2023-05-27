package com.market.index.service.impl;

import com.google.common.collect.Lists;
import com.market.activity.mapper.ActivityMapper;
import com.market.activity.mapper.GrantMapper;
import com.market.common.utils.StringUtils;
import com.market.coupon.mapper.CouponMapper;
import com.market.coupon.model.CouponUseEntity;
import com.market.index.model.IndexParam;
import com.market.index.model.SummaryInfo;
import com.market.index.service.IndexService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 14:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IndexServiceImpl implements IndexService {

    private static final String STR_SPLIT = "~";

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private GrantMapper grantMapper;

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 获取统计数据
     * @param param
     * @return
     */
    @Override
    public SummaryInfo getSummaryData(IndexParam param) {
        String startTime = Strings.EMPTY;
        String endTime = Strings.EMPTY;
        if(Objects.nonNull(param.getParams().get("beginTime"))){
            startTime = param.getParams().get("beginTime") + " 00:00:00";
        }
        if(Objects.nonNull(param.getParams().get("endTime"))){
            endTime = param.getParams().get("endTime") + " 23:59:59";
        }
        param.setBeginTime(startTime);
        param.setEndTime(endTime);
        //活动数
        int activityNum = activityMapper.getActivityNum(param);
        //发放券数量
        int sendCouponNum = grantMapper.getSendNum(param);
        //领取券数量
        int receiveNum = couponMapper.getReceiveNum(param);
        //使用券数量
        int useNum = couponMapper.getUseNum(param);
        Double receiveRate = getRate(receiveNum, sendCouponNum);
        Double useRate = getRate(useNum, sendCouponNum);

        //优惠券使用详情汇总
        CouponUseEntity entity = couponMapper.getCouponUseDetail(param);
        //获取拉新数
        int newNum = couponMapper.getNewNum(param);
        SummaryInfo info = new SummaryInfo();
        info.setActivityNum(activityNum);
        info.setSendCouponNum(sendCouponNum);
        info.setReceiveCouponNum(receiveNum);
        info.setReceiveRate(receiveRate);
        info.setUseRate(useRate);
        info.setAmount(Objects.isNull(entity.getOrderAmount()) ? BigDecimal.ZERO : entity.getOrderAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        info.setOrderNum(entity.getOrderNum());
        info.setOrderPrice(getOrderPrice(entity.getOrderAmount(), entity.getOrderNum()));
        info.setNewNum(newNum);
        info.setUseNum(useNum);
        return info;
    }

    /**
     * 获取用券笔单价(用券总成交额/使用优惠券的付款订单总数)
     * @param orderAmount
     * @param orderNum
     * @return
     */
    private BigDecimal getOrderPrice(BigDecimal orderAmount, Integer orderNum) {
        if(Objects.isNull(orderAmount) || orderNum == 0){
            return BigDecimal.ZERO;
        }
        return orderAmount.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取统计列表数据
     * @param param
     * @return
     */
    @Override
    public List<SummaryInfo> getSummaryListData(IndexParam param) {
        List<SummaryInfo> list = couponMapper.getSummaryListData(param);
        if(CollectionUtils.isEmpty(list)){
            return list;
        }
        //未关联活动的优惠券列表
        List<SummaryInfo> noConList = list.stream().filter(e -> StringUtils.isEmpty(e.getActivityId())).collect(Collectors.toList());
        //已关联活动的优惠券列表
        List<SummaryInfo> conList = list.stream().filter(e -> StringUtils.isNotEmpty(e.getActivityId())).collect(Collectors.toList());
        //优惠券id
        Set<String> couponIdSet = conList.stream().map(SummaryInfo::getCouponId).collect(Collectors.toSet());
        //活动id
        Set<String> activityIdSet = conList.stream().map(SummaryInfo::getActivityId).collect(Collectors.toSet());
        List<String> couponIdList = Lists.newArrayList(couponIdSet);
        List<String> activityIdList = Lists.newArrayList(activityIdSet);
        param.setCouponIdList(couponIdList);
        param.setActivityIdList(activityIdList);
        //获取优惠券使用数量
        List<SummaryInfo> useList = couponMapper.getUseNumByCouponIds(param);
        Map<String, List<SummaryInfo>> map = null;
        if(CollectionUtils.isNotEmpty(useList)){
            map = useList.stream().collect(Collectors.groupingBy(e -> {
                return e.getActivityId() + STR_SPLIT + e.getCouponId();
            }));
        }
        //获取优惠券使用订单数据
        List<CouponUseEntity> entityList = couponMapper.getCouponUseDetailList(param);
        Map<String, List<CouponUseEntity>> orderMap = null;
        if(CollectionUtils.isNotEmpty(entityList)){
            orderMap = entityList.stream().collect(Collectors.groupingBy(e -> {
                return e.getActivityId() + STR_SPLIT + e.getCouponId();
            }));
        }
        //拉新数
        List<CouponUseEntity> newList = couponMapper.getNewNumByGroup(param);
        Map<String, List<CouponUseEntity>> newMap = null;
        if(CollectionUtils.isNotEmpty(newList)){
            newMap = newList.stream().collect(Collectors.groupingBy(e -> {
                return e.getActivityId() + STR_SPLIT + e.getCouponId();
            }));
        }
        Map<String, List<SummaryInfo>> finalMap = map;
        Map<String, List<CouponUseEntity>> finalOrderMap = orderMap;
        Map<String, List<CouponUseEntity>> finalNewMap = newMap;
        conList.forEach(e -> {
            e.setReceiveRate(getRate(e.getReceiveCouponNum(), e.getSendCouponNum()));
            //使用数和使用率
            if(Objects.nonNull(finalMap)){
                List<SummaryInfo> infos = finalMap.get(e.getActivityId() + STR_SPLIT + e.getCouponId());
                if(CollectionUtils.isNotEmpty(infos)){
                    int sum = infos.stream().mapToInt(SummaryInfo::getUseNum).sum();
                    e.setUseNum(sum);
                    e.setUseRate(getRate(sum, e.getSendCouponNum()));
                }
            }
            //使用订单总额、订单使用优惠总额、使用优惠券订单数
            if(Objects.nonNull(finalOrderMap)){
                List<CouponUseEntity> useEntityList = finalOrderMap.get(e.getActivityId() + STR_SPLIT + e.getCouponId());
                if(CollectionUtils.isNotEmpty(useEntityList)){
                    BigDecimal orderAmount = useEntityList.stream().map(CouponUseEntity::getOrderAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal disAmount = useEntityList.stream().map(CouponUseEntity::getDisAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    int orderNum = useEntityList.stream().mapToInt(CouponUseEntity::getOrderNum).sum();
                    e.setAmount(orderAmount);
                    e.setDisAmount(disAmount);
                    e.setOrderNum(orderNum);
                    e.setOrderPrice(getOrderPrice(orderAmount, orderNum));
                }
            }
            //拉新数
            if(Objects.nonNull(finalNewMap)){
                List<CouponUseEntity> newList1 = finalNewMap.get(e.getActivityId() + STR_SPLIT + e.getCouponId());
                e.setNewNum(Objects.isNull(newList1) ? 0 : newList1.size());
            }
        });
        conList.addAll(noConList);
        return conList;
    }

    private Double getRate(int num1, int num2) {
        if(num2 == 0){
            return 0.0D;
        }
        BigDecimal data = new BigDecimal(num1).divide(new BigDecimal(num2), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        return data.doubleValue();
    }
}
