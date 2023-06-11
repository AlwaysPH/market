package com.market.thread.job;

import com.market.common.enums.CouponUseStatusEnum;
import com.market.common.enums.ReceiveTypeEnum;
import com.market.coupon.mapper.CouponMapper;
import com.market.coupon.model.UserCouponInfo;
import com.market.thread.model.GrantCouponInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 发放优惠券异步线程
 *
 * @author ph
 * @version 1.0
 * @date 2023-06-04 13:10
 */
public class GrantCouponToUserJob implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GrantCouponToUserJob.class);

    private GrantCouponInfo info;

    private CouponMapper couponMapper;

    public GrantCouponToUserJob(GrantCouponInfo info, CouponMapper couponMapper) {
        this.info = info;
        this.couponMapper = couponMapper;
    }

    @Override
    public void run() {
        log.info("开始执行发放优惠券线程......");
        if (Objects.isNull(info)) {
            log.info("无数据，不执行发放优惠券线程！");
            return;
        }
        if (CollectionUtils.isEmpty(info.getUserInfoList())) {
            log.info("无用户数据，不执行发放优惠券线程！");
            return;
        }
        if (info.getGrantNum().intValue() == 0) {
            log.info("发放数量为0，不执行发放优惠券线程！");
            return;
        }
        List<UserCouponInfo> list = Lists.newArrayList();
        List<GrantCouponInfo.UserInfo> userInfoList = info.getUserInfoList();
        userInfoList.forEach(e -> {
            UserCouponInfo data = new UserCouponInfo();
            BeanUtils.copyProperties(e, info);
            data.setActivityId(info.getActivityId());
            data.setCouponId(info.getCouponId());
            data.setChannelType(info.getChannelType());
            data.setSendType(info.getSendType());
            data.setStatus(CouponUseStatusEnum.UN_USED.getCode());
            data.setReceiveTime(new Date());
            data.setAppAccount(data.getPhoneNumber());
            list.add(data);
        });
        if (CollectionUtils.isNotEmpty(list)) {
            //数据切分，分批次入库
            List<List<UserCouponInfo>> data = split(list, info.getSplitNum());
            data.forEach(e -> {
                couponMapper.batchCouponUser(e);
            });
        }
    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size, int input) {
        return (size + input - 1) / input;
    }

    /**
     * @param list  需要分隔的 集合
     * @param input 指定分隔size
     * @return
     */
    public static List<List<UserCouponInfo>> split(List<UserCouponInfo> list, int input) {
        int limit = countStep(list.size(), input);
        List<List<UserCouponInfo>> splitList;
        splitList = Stream.iterate(0, n -> n + 1).limit(limit).
                map(a -> list.stream().skip(a * input).limit(input).collect(Collectors.toList())).
                collect(Collectors.toList());
        //当输入数量小于分隔数量需反转
        if (input < limit) {
            splitList = Stream.iterate(0, n -> n + 1).limit(input).
                    map(a -> list.stream().skip(a * limit).limit(limit).collect(Collectors.toList())).
                    collect(Collectors.toList());
        }
        return splitList;
    }
}
