package com.franroa.roottranslator.queue;

import org.quartz.Trigger;

import java.util.Date;

public interface Queue {
    void start();
    void stop();
    void push(Job job);
    void push(Job job, Date schedule);
    void push(Job job, Trigger trigger);
}
