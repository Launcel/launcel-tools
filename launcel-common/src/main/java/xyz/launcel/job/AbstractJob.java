/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import xyz.launcel.job.context.Jobs;
import xyz.launcel.job.orm.JobDbSupport;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.StringUtils;

import java.util.Objects;

public abstract class AbstractJob implements InitializingBean
{
    @Getter
    private Job job;

    protected abstract Runnable work();

    protected abstract String getJobName();

    protected boolean canWork()
    {
        job = getCurrentJob();
        job.setWork(work());
        return Objects.nonNull(job);
    }

    protected void registerJob()
    {
        if (canWork())
        {
            Jobs.add(this.job);
        }
    }

    public void stop()
    {
        Jobs.remove(job.getId());
    }

    public void reset()
    {
        Jobs.remove(job.id);
        job = getCurrentJob();
        job.setWork(work());
        if (Objects.nonNull(job))
        {
            Jobs.add(job);
        }
    }

    @Override
    public void afterPropertiesSet()
    {
        registerJob();
    }

    private Job getCurrentJob()
    {
        var entitys = JobDbSupport.query("select * from " + JobDbSupport.getTableName() + " where job_name=?", new Object[]{getJobName()});
        if (CollectionUtils.isEmpty(entitys) || entitys.size() > 1)
        {
            return null;
        }
        var entity = entitys.get(0);
        if (entity.getStatus() == - 1 || ! entity.getEnabled() || StringUtils.isBlank(entity.getCron()))
        {
            return null;
        }
        return Job.builder().id(entity.getId()).cron(entity.getCron()).jobName(entity.getJobName()).status(entity.getStatus()).build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    public static class Job
    {
        private Integer  id;
        private String   jobName;
        private String   cron;
        private Short    status;
        private Runnable work;
    }
}
