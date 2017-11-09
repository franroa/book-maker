package com.franroa.roottranslator.queue;

import com.franroa.roottranslator.queue.config.QueueConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzQueue implements Queue {
    private Scheduler scheduler;

    public QuartzQueue(QueueConfiguration configuration) {
        try {
            Properties properties = new Properties();
            properties.putAll(configuration.quartz);

            scheduler = new StdSchedulerFactory(properties).getScheduler();
        } catch (SchedulerException e) {
            throw new WebApplicationException("Could not initiate Quartz scheduler properly", e);
        }
    }

    @Override
    public void start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new WebApplicationException("Could not start the Quartz scheduler properly", e);
        }
    }

    @Override
    public void stop() {
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            throw new WebApplicationException("Could not shutdown the Quartz scheduler properly", e);
        }
    }

    @Override
    public void push(Job job) {
        push(job, newTrigger().startNow().build());
    }

    @Override
    public void push(Job job, Date start) {
        push(job, newTrigger().startAt(start).build());
    }

    @Override
    public void push(Job job, Trigger trigger) {
        JobDetail detail = newJob(job.getClass()).usingJobData(new JobDataMap(job.getData())).build();

        try {
            scheduler.scheduleJob(detail, trigger);
        } catch (SchedulerException e) {
            throw new WebApplicationException("Failed to schedule a job into the Quartz queue", e);
        }
    }
}
