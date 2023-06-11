package com.market.scheduled.task;

import com.google.common.collect.Lists;
import com.market.common.enums.CouponEffectTypeEnum;
import com.market.common.enums.CouponUseStatusEnum;
import com.market.common.utils.DateUtils;
import com.market.common.utils.StringUtils;
import com.market.coupon.mapper.CouponMapper;
import com.market.coupon.model.CouponTimeOut;
import com.market.scheduled.QuartzScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户优惠券失效任务
 * @author ph
 * @version 1.0
 * @date 2023-05-21 16:53
 */
@Component
@DisallowConcurrentExecution
public class CouponTimeOutTask implements QuartzScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(CouponTimeOutTask.class);

    private static final String KEY = "CouponTimeOutTask";

    private static final String LOCK = "CouponTimeOutTask_LOCK";

    private static final List<String> STATUS = Arrays.asList(CouponUseStatusEnum.UN_EFFECT.getCode(),
            CouponUseStatusEnum.UN_USED.getCode(), CouponUseStatusEnum.BACK.getCode());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public void execute() {
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(KEY, LOCK, 30, TimeUnit.SECONDS);
            log.info("执行用户优惠券失效任务是否获取到锁:" + lock);
            if(lock){
                log.info("开始用户优惠券失效任务......");
                executeTask();
            }else {
                log.info("执行用户优惠券失效任务没有获取到锁，不执行任务!");
            }
        } catch (Exception e) {
            log.error("执行用户优惠券失效任务异常!", e);
        }finally {
            if(lock){
                redisTemplate.delete(KEY);
                log.info("执行用户优惠券失效任务结束，释放锁!");
            }
        }
    }

    private void executeTask() {
        //获取未使用和未生效的优惠券列表
        List<CouponTimeOut> list = couponMapper.getTimeOutList(STATUS);
        if(CollectionUtils.isEmpty(list)){
            log.info("无未使用、未生效和退回的优惠券数据");
            return;
        }
        List<String> idList = Lists.newArrayList();
        for(CouponTimeOut data : list){
            //活动结束日期为空
            if(Objects.isNull(data.getEndTime())){
                idList.add(data.getUserCouponId());
                continue;
            }
            //当前时间大于活动结束时间
            if(System.currentTimeMillis() >= data.getEndTime().getTime()){
                idList.add(data.getUserCouponId());
                continue;
            }
            if (StringUtils.equalsIgnoreCase(data.getEffectType(), CouponEffectTypeEnum.FIX.getCode())) {
                /**固定日期**/
                if(Objects.isNull(data.getEffectEndTime())){
                    idList.add(data.getUserCouponId());
                    continue;
                }
                //当前时间大于券有效期结束
                if(System.currentTimeMillis() >= data.getEffectEndTime().getTime()){
                    idList.add(data.getUserCouponId());
                    continue;
                }
            }else if(StringUtils.equalsIgnoreCase(data.getEffectType(), CouponEffectTypeEnum.ACC.getCode())){
                /**累计日期**/
                if(Objects.isNull(data.getReceiveTime()) || Objects.isNull(data.getTakeDateNum())
                    || Objects.isNull(data.getEffectDateNum())){
                    idList.add(data.getUserCouponId());
                    continue;
                }
                Date afterDate = DateUtils.daysAgoOrAfterToDate(data.getReceiveTime(), data.getEffectDateNum() + data.getTakeDateNum());
                if(System.currentTimeMillis() >= afterDate.getTime()){
                    idList.add(data.getUserCouponId());
                    continue;
                }
            }
        }
        if(CollectionUtils.isNotEmpty(idList)){
            //更新已失效的优惠券
            couponMapper.updateTimeOut(idList, CouponUseStatusEnum.TIME_OUT.getCode());
        }
    }
}
