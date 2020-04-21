package ru.job4j.parser;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;

public class QuartzMain implements Grab {

    public static void main(String[] args) throws SchedulerException {

        Config config = new Config();
        config.init();

        JobDetail job = JobBuilder.newJob(ExecuteParser.class).build();
        Trigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("CronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(config.get("cron.time")))
                .build();
        Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
        sc.start();
        sc.scheduleJob(job, cronTrigger);
    }

    @Override
    public void init(Parse parse, Store store) {

    }
}