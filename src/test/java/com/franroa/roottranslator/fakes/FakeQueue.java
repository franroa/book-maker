package com.franroa.roottranslator.fakes;

import com.franroa.roottranslator.queue.Job;
import com.franroa.roottranslator.queue.Queue;
import org.junit.Assert;
import org.quartz.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

public class FakeQueue implements Queue {
    private ArrayList<Job> queuedJobs = new ArrayList<>();

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void push(Job job) {
        queuedJobs.add(job);
    }

    @Override
    public void push(Job job, Date schedule) {
        push(job);
    }

    @Override
    public void push(Job job, Trigger trigger) {
        push(job);
    }

    public <T extends Job> void assertPushed(Class<T> jobClass, Function<T, Boolean> compareFunction) {
        for (Job queuedJob: queuedJobs) {
            if (queuedJob.getClass().equals(jobClass) && compareFunction.apply((T) queuedJob)) {
                return;
            }
        }

        Assert.fail("Job was not pushed into the queue!");
    }

    public <T extends Job> void assertPushed(Class<T> jobClass) {
        assertPushed(jobClass, job -> true);
    }
}
