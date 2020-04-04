package ru.job4j.parser;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {

    public static void main(String[] args) throws SchedulerException {
        Config config = new Config();
        config.init();

        JobDetail job = JobBuilder.newJob(Parser.class).build();
        Trigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("CronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(config.get("cron.time")))
                .build();
        Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
        sc.start();
        sc.scheduleJob(job, cronTrigger);
    }
}
