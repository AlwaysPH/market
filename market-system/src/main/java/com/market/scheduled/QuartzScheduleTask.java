package com.market.scheduled;

/**
 * 定时任务接口
 * @author PH
 * @version 1.0
 * @date 2023-05-21 16:53
 */
public interface QuartzScheduleTask {

    /**
     * 执行方法
     */
    void execute();
}
