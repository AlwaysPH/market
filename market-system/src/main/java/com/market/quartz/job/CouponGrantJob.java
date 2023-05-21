//package com.market.quartz.job;
//
//import com.market.quartz.QuartzScheduleTask;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.InterruptableJob;
//import org.quartz.JobExecutionContext;
//import org.quartz.UnableToInterruptJobException;
//import org.springframework.stereotype.Component;
//
///**
// * 优惠券自动发放
// * @author ph
// * @version 1.0
// * @date 2023-05-06 11:15
// */
//@Component
//@Slf4j
//public class CouponGrantJob extends QuartzScheduleTask implements InterruptableJob {
//
//    /**
//     * 优惠券自动发放分组名称
//     */
//    public static final String COUPON_JOB_GROUP = "couponGrantTask";
//
//    /**
//     * job 是否中断
//     */
//    private boolean interrupted = false;
//
//    @Override
//    protected void executeJob(JobExecutionContext context) throws Exception {
//
//    }
//
//    @Override
//    public void interrupt() throws UnableToInterruptJobException {
//        interrupted = true;
//    }
//}
