package com.franroa.roottranslator.queue;

import org.quartz.Trigger;

import java.util.Date;

public class SyncQueue implements Queue {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void push(Job job) {
        job.handle();
    }

    @Override
    public void push(Job job, Date schedule) {
        push(job);
    }

    @Override
    public void push(Job job, Trigger trigger) {
        push(job);
    }
}
