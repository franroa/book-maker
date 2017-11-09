package com.franroa.roottranslator.queue;

import com.franroa.roottranslator.config.Connection;
import com.franroa.roottranslator.container.Container;
import org.quartz.*;
import org.quartz.spi.OperableTrigger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Job implements org.quartz.Job {
    protected Map<String, Object> data = new HashMap<>();
    protected Integer maxRetries = 0;

    public Job dispatch() {
        Container.resolve(Queue.class).push(this);

        return this;
    }

    public Job dispatch(Date schedule) {
        Container.resolve(Queue.class).push(this, schedule);

        return this;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        data = jobExecutionContext.getJobDetail().getJobDataMap().getWrappedMap();

        try {
            Connection.open();
            handle();
        } catch (Exception e) {
            retry(jobExecutionContext);
        } finally {
            Connection.close();
        }
    }

    public Job set(String key, Object value) {
        data.put(key, value);

        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    private void retry(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int retries = jobDataMap.containsKey("retries") ? jobDataMap.getIntValue("retries") : 0;

        Date nextRetryAt = new Date(jobExecutionContext.getFireTime().getTime() + (retries * 1000));

        if (retries == maxRetries) {
            //Container.resolve(EmailService.class).send(new JobFailedMail(jobExecutionContext.getJobDetail()));
            nextRetryAt = DateBuilder.futureDate(50, DateBuilder.IntervalUnit.YEAR);
        }

        jobDataMap.put("retries", retries + 1);

        JobDetail job = jobExecutionContext
                .getJobDetail()
                .getJobBuilder()
                .withIdentity(jobExecutionContext.getJobDetail().getKey().getName() + " - " + retries, "FailingJobsGroup")
                .usingJobData(jobDataMap)
                .build();

        OperableTrigger trigger = (OperableTrigger) TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withIdentity(jobExecutionContext.getTrigger().getKey().getName() + " - " + retries, "FailingJobsGroup")
                .startAt(nextRetryAt)
                .build();

        try {
            jobExecutionContext.getScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new JobExecutionException(e);
        }
    }

    protected abstract void handle();
}
