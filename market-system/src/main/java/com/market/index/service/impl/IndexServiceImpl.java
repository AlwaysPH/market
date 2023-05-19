package com.market.index.service.impl;

import com.market.activity.mapper.ActivityMapper;
import com.market.activity.mapper.GrantMapper;
import com.market.common.utils.DateUtils;
import com.market.common.utils.StringUtils;
import com.market.coupon.mapper.CouponMapper;
import com.market.index.model.IndexParam;
import com.market.index.model.SummaryInfo;
import com.market.index.service.IndexService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-14 14:11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IndexServiceImpl implements IndexService {

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

        SummaryInfo info = new SummaryInfo();
        info.setActivityNum(activityNum);
        info.setSendCouponNum(sendCouponNum);
        info.setReceiveCouponNum(receiveNum);
        info.setReceiveRate(receiveRate);
        info.setUseRate(useRate);
//        info.setAmount();
//        info.setOrderNum();
//        info.setOrderPrice();
//        info.setNewNum();
//        info.setUseNum();
        return info;
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
        List<String> idList = list.stream().map(SummaryInfo::getCouponId).collect(Collectors.toList());
        param.setIdList(idList);
        List<SummaryInfo> useList = couponMapper.getUseNumByCouponIds(param);
        Map<String, List<SummaryInfo>> map = null;
        if(CollectionUtils.isNotEmpty(useList)){
            map = useList.stream().collect(Collectors.groupingBy(SummaryInfo::getCouponId));
        }
        Map<String, List<SummaryInfo>> finalMap = map;
        list.forEach(e -> {
            e.setReceiveRate(getRate(e.getReceiveCouponNum(), e.getSendCouponNum()));
            if(Objects.nonNull(finalMap)){
                List<SummaryInfo> infos = finalMap.get(e.getCouponId());
                if(CollectionUtils.isNotEmpty(infos)){
                    int sum = infos.stream().mapToInt(SummaryInfo::getUseNum).sum();
                    e.setUseRate(getRate(sum, e.getSendCouponNum()));
                }
            }
        });
        return list;
    }

    private Double getRate(int num1, int num2) {
        if(num2 == 0){
            return 0.0D;
        }
        BigDecimal data = new BigDecimal(num1).divide(new BigDecimal(num2), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        return data.doubleValue();
    }
}
