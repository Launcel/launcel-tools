/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import lombok.Getter;
import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import xyz.launcel.job.context.Jobs;
import xyz.launcel.job.orm.JobDbSupport;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

public abstract class AbstractJob implements InitializingBean
{

    @Inject
    @Named("scheduler")
    @Getter
    private ThreadPoolTaskScheduler scheduler;
    @Getter
    private Job                     job;

    protected boolean canWork()
    {
        var entitys = JobDbSupport.query("select * from " + JobDbSupport.getTableName() + " where job_name=?", new Object[]{getJobName()});
        if (CollectionUtils.isEmpty(entitys) || entitys.size() > 1)
        {
            return false;
        }
        var entity = entitys.get(0);
        if (entity.getStatus() == - 1 || ! entity.getEnabled() || StringUtils.isBlank(entity.getCron()))
        {
            return false;
        }
        if (Objects.isNull(job))
        {
            job = new Job();
        }
        job.cron = entity.getCron();
        job.id = entity.getId();
        job.status = entity.getStatus();
        return true;
    }

    protected void register()
    {
        if (canWork())
        {
            Jobs.add(job.getId(), this.getFuture());
        }
    }

    public void stop()
    {
        Jobs.stop(job.getId());
    }

    public void reset()
    {
        job = getCurrentJob();
        if (Objects.nonNull(job))
        {
            Jobs.reset(job.getId(), this.getFuture());
        }
    }

    protected Job getCurrentJob()
    {
        var entitys = JobDbSupport.query("select * from " + JobDbSupport.getTableName() + " where id=? limit 1", new Object[]{job.getId()});
        if (CollectionUtils.isEmpty(entitys) || entitys.size() > 1)
        {
            return null;
        }
        var entity = entitys.get(0);
        if (entity.getStatus() == - 1 || ! entity.getEnabled() || StringUtils.isBlank(entity.getCron()))
        {
            return null;
        }
        job.cron = entity.getCron();
        job.id = entity.getId();
        job.jobName = entity.getJobName();
        job.status = entity.getStatus();
        return job;
    }

    protected ScheduledFuture getFuture()
    {
        return scheduler.schedule(work(), new CronTrigger(this.job.getCron()));
    }

    @Override
    public void afterPropertiesSet()
    {
        register();
    }

    protected abstract Runnable work();

    protected abstract String getJobName();

    @Getter
    public static class Job
    {
        private Integer id;
        private String  jobName;
        private String  cron;
        private Short   status;
    }
}
