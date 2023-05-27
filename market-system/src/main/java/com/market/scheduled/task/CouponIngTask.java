package com.market.scheduled.task;

import com.google.common.collect.Lists;
import com.market.common.enums.CouponEffectTypeEnum;
import com.market.common.enums.CouponUseStatusEnum;
import com.market.common.utils.DateUtils;
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
import java.util.concurrent.TimeUnit;

/**
 * 用户优惠券生效任务
 * @author ph
 * @version 1.0
 * @date 2023-05-21 16:53
 */
@Component
@DisallowConcurrentExecution
public class CouponIngTask implements QuartzScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(CouponIngTask.class);

    private static final String KEY = "CouponIngTask";

    private static final String LOCK = "CouponIngTask_LOCK";

    private static final List<String> STATUS = Arrays.asList(CouponUseStatusEnum.UN_EFFECT.getCode());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public void execute() {
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(KEY, LOCK, 30, TimeUnit.SECONDS);
            log.info("执行用户优惠券生效任务是否获取到锁:" + lock);
            if(lock){
                log.info("开始用户优惠券生效任务......");
                executeTask();
            }else {
                log.info("执行用户优惠券生效任务没有获取到锁，不执行任务!");
            }
        } catch (Exception e) {
            log.error("执行用户优惠券生效任务异常!", e);
        }finally {
            if(lock){
                redisTemplate.delete(KEY);
                log.info("执行用户优惠券生效任务结束，释放锁!");
            }
        }
    }

    private void executeTask() {
        //获取还未生效的优惠券列表
        List<CouponTimeOut> list = couponMapper.getTimeOutList(STATUS);
        if(CollectionUtils.isEmpty(list)){
            log.info("无未生效优惠券数据");
            return;
        }
        List<String> idList = Lists.newArrayList();
        for(CouponTimeOut data : list){
            if(System.currentTimeMillis() >= data.getStartTime().getTime()
                && System.currentTimeMillis() < data.getEndTime().getTime()){
                if (com.market.common.utils.StringUtils.equalsIgnoreCase(data.getEffectType(), CouponEffectTypeEnum.FIX.getCode())) {
                    /**固定日期**/
                    if(System.currentTimeMillis() >= data.getEffectStartTime().getTime()
                            && System.currentTimeMillis() < data.getEffectEndTime().getTime()){
                        idList.add(data.getUserCouponId());
                    }
                }else if(com.market.common.utils.StringUtils.equalsIgnoreCase(data.getEffectType(), CouponEffectTypeEnum.ACC.getCode())){
                    /**累计日期**/
                    Date afterDate = DateUtils.daysAgoOrAfterToDate(data.getReceiveTime(), data.getEffectDateNum() + data.getTakeDateNum());
                    if(System.currentTimeMillis() < afterDate.getTime()){
                        idList.add(data.getUserCouponId());
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(idList)){
            //更新未生效的优惠券为未使用
            couponMapper.updateTimeOut(idList, CouponUseStatusEnum.UN_USED.getCode());
        }
    }
}
