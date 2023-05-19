package com.market.quartz;

import com.market.common.core.redis.RedisCache;
import com.market.common.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-06 09:58
 */
@Log4j2
public abstract class QuartzScheduleTask extends QuartzJobBean {

    private static final String JOB_STATUS_KEY_PREFIX = "JOB_STATUS_";

    private static final String JOB_STATUS_RUNNING = "RUNNING";

    @Autowired
    protected RedisCache redisCache;

    /**
     * 子类实现
     * @param context
     * @throws Exception
     */
    protected abstract void executeJob(JobExecutionContext context) throws Exception;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String statusKey = JOB_STATUS_KEY_PREFIX + context.getJobDetail().getKey();
        String jobStatus = String.valueOf(redisCache.getCacheObject(statusKey));
        log.info("任务:{}, 当前状态:{}", statusKey, jobStatus);
        if(StringUtils.equalsIgnoreCase(jobStatus, JOB_STATUS_RUNNING)){
            log.info("任务:{}, 正在运行中，忽略本次调度...", statusKey);
            return;
        }
        redisCache.setCacheObject(statusKey, JOB_STATUS_RUNNING);
        try{
            this.executeJob(context);
        }catch (Exception e){
            log.error("执行任务失败： ", e);
        }finally {
            redisCache.deleteObject(statusKey);
            log.info("任务:{}, 执行结束", statusKey);
        }
    }
}
