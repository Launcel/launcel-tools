/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import lombok.Getter;
import lombok.Setter;
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

public abstract class AbstractJob implements InitializingBean
{

    @Inject
    @Named("scheduler")
    private ThreadPoolTaskScheduler scheduler;
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
        job.setCron(entity.getCron());
        job.setId(entity.getId());
        job.setStatus(entity.getStatus());
        return true;
    }

    protected void register()
    {
        if (canWork())
        {
            var future = scheduler.schedule(work(), new CronTrigger(this.job.getCron()));
            Jobs.add(job.getId(), future);
        }
    }

    protected void stop()
    {
        Jobs.stop(job.getId());
    }

    protected void reset()
    {
        job = getCurrentJob();
        if (Objects.nonNull(job))
        {
            var future = scheduler.schedule(work(), new CronTrigger(this.job.getCron()));
            Jobs.reset(job.getId(), future);
        }
    }

    @Override
    public void afterPropertiesSet()
    {
        register();
    }

    protected abstract Runnable work();

    protected abstract String getJobName();

    protected Integer getJobId()
    {
        return job.getId();
    }

    protected String getCron()
    {
        return job.getCron();
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
        job.setCron(entity.getCron());
        job.setId(entity.getId());
        job.setJobName(entity.getJobName());
        job.setStatus(entity.getStatus());
        return job;
    }

    protected void setJob(Job job)
    {
        this.job = job;
    }

    protected Job getJob()
    {
        return job;
    }

    @Getter
    @Setter
    public static class Job
    {
        private Integer id;
        private String  jobName;
        private String  cron;
        private Short   status;
    }
}
