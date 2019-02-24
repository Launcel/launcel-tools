/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import xyz.launcel.job.orm.JobDbSupport;
import xyz.launcel.utils.CollectionUtils;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class AbstractJob implements InitializingBean
{
    @Inject
    @Named(value = "scheduler")
    private ThreadPoolTaskScheduler scheduler;

    private String jobName;

    private String corn;

    protected abstract void work();

    protected boolean canWork()
    {
        var entitys = JobDbSupport.query("select * from " + JobDbSupport.getTableName() + " where job_name=?", new Object[]{getJobName()});
        if (CollectionUtils.isEmpty(entitys))
        {
            return false;
        }
        var entity = entitys.get(0);
        if (entity.getStatus() == - 1 || ! entity.getEnabled())
        {
            return false;
        }
        setJobName(entity.getJobName());
        setCorn(entity.getCron());
        return true;
    }

    protected void registerJob()
    {
        var future = scheduler.schedule(() -> {
            if (canWork())
            {
                work();
            }
        }, new CronTrigger(getCorn()));
    }

    @Override
    public void afterPropertiesSet()
    {
        registerJob();
    }

    public void setCorn(String corn)
    {
        this.corn = corn;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getCorn()
    {
        return corn;
    }

    public String getJobName()
    {
        return jobName;
    }
}
