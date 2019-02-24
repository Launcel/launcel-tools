/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public abstract class AbstractJob
{

    public abstract String getJobName();

    public abstract String getCorn();

    public abstract void process(Runnable r);

    public void register()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        ScheduledFuture future = scheduler.schedule(() -> {
        }, new CronTrigger(getCorn()));
    }
}
