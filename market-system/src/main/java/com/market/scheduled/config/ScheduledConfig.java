package com.market.scheduled.config;

import com.market.scheduled.QuartzScheduleTask;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @author ph
 * @version 1.0
 * @date 2023-05-21 16:49
 */
//@Configuration
public class ScheduledConfig {

    private static final String THREAD_COUNT = "org.quartz.threadPool.threadCount";
    private static final String THREAD_NAME_PREFIX = "org.quartz.threadPool.threadNamePrefix";
    private static final String SPLIT_STR1 = "\\\\";
    private static final String SPLIT_STR2 = ":";

    @Value("${quartz.schedulerName}")
    private String schedulerName;

    @Value("${quartz.threadCount}")
    private String threadCount;

    @Value("${quartz.threadNamePrefix}")
    private String threadNamePrefix;

    @Value("${quartz.tasks}")
    private String tasks;

    private final ApplicationContext context;

    @Autowired
    public ScheduledConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        Properties properties = new Properties();
        properties.setProperty(THREAD_COUNT, threadCount);
        properties.setProperty(THREAD_NAME_PREFIX, threadNamePrefix);
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName(schedulerName);
        factory.setQuartzProperties(properties);
        return factory;
    }

    @Bean
    public Scheduler scheduler() throws Exception {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        scheduler.scheduleJobs(createJobDetails(), true);
        return scheduler;
    }

    /**
     * 创建JobDetail
     * @return Map<JobDetail, Set < CronTrigger>>
     */
    private Map<JobDetail, Set<? extends Trigger>> createJobDetails() throws NoSuchMethodException, ClassNotFoundException, ParseException {
        List<String> taskSet = Arrays.asList(tasks.split(SPLIT_STR1));
        Map<JobDetail, Set<? extends Trigger>> map = new HashMap<>(taskSet.size());
        for (String task : taskSet) {
            String[] nameAndCron = task.split(SPLIT_STR2);
            String name = StringUtils.uncapitalize(nameAndCron[0]);
            String cron = nameAndCron[1];
            MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
            factoryBean.setTargetObject(context.getBean(name));
            factoryBean.setName(name);
            factoryBean.setTargetMethod(QuartzScheduleTask.class.getMethods()[0].getName());
            factoryBean.afterPropertiesSet();
            CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
            cronTriggerFactoryBean.setCronExpression(cron);
            cronTriggerFactoryBean.setJobDetail(factoryBean.getObject());
            cronTriggerFactoryBean.setName(name);
            cronTriggerFactoryBean.afterPropertiesSet();
            Set<CronTrigger> cronTriggerSet = new HashSet<>(1);
            cronTriggerSet.add(cronTriggerFactoryBean.getObject());
            map.put(factoryBean.getObject(), cronTriggerSet);
        }
        return map;
    }
}
