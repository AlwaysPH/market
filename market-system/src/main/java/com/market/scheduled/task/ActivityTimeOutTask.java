package com.market.scheduled.task;

import com.market.activity.mapper.ActivityMapper;
import com.market.activity.model.ActivityInfo;
import com.market.common.enums.ActivityStatusEnum;
import com.market.scheduled.QuartzScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 活动失效任务
 * @author ph
 * @version 1.0
 * @date 2023-05-21 17:46
 */
@Component
@DisallowConcurrentExecution
public class ActivityTimeOutTask implements QuartzScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ActivityTimeOutTask.class);

    private static final String KEY = "ActivityTimeOutTask";

    private static final String LOCK = "ActivityTimeOutTask_LOCK";

    private static final List<String> STATUS = Arrays.asList(ActivityStatusEnum.NO_START.getCode(),
                                                ActivityStatusEnum.ING.getCode(), ActivityStatusEnum.STOP.getCode());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public void execute() {
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(KEY, LOCK, 30, TimeUnit.SECONDS);
            log.info("执行活动失效任务是否获取到锁:" + lock);
            if(lock){
                log.info("开始活动失效任务......");
                executeTask();
            }else {
                log.info("执行活动失效任务没有获取到锁，不执行任务!");
            }
        } catch (Exception e) {
            log.error("执行活动失效任务异常!", e);
        }finally {
            if(lock){
                redisTemplate.delete(KEY);
                log.info("执行活动失效任务结束，释放锁!");
        }
        }
    }

    private void executeTask() {
        //获取未开始、进行中、已停止的活动列表
        List<ActivityInfo> list = activityMapper.getTimeOutList(STATUS);
        if(CollectionUtils.isEmpty(list)){
            log.info("无未开始、进行中、已停止的活动数据");
            return;
        }
        List<String> idList = Lists.newArrayList();
        list.forEach(e -> {
            if(System.currentTimeMillis() >= e.getEndTime().getTime()){
                idList.add(e.getId());
            }
        });
        if(CollectionUtils.isNotEmpty(idList)){
            activityMapper.updateInfoList(idList, ActivityStatusEnum.FAILURE.getCode());
        }
    }
}
