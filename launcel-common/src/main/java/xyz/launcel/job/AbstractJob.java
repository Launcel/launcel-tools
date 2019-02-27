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
import xyz.launcel.job.context.Jobs;
import xyz.launcel.job.orm.JobDbSupport;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Objects;

public abstract class AbstractJob
{

    private Job job = new Job();

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
        job.setCron(entity.getCron());
        job.setId(entity.getId());
        job.setStatus(entity.getStatus());
        return true;
    }

    protected void registerJob()
    {
        if (canWork())
        {
            Jobs.add(job, work());
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
            Jobs.reset(job, work());
        }
    }

    @PostConstruct
    public void init()
    {
        registerJob();
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
