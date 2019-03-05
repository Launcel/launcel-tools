/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.4
 * Version: 1.0
 */

package xyz.launcel.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import xyz.launcel.job.bean.Job;
import xyz.launcel.job.context.Jobs;
import xyz.launcel.job.orm.JobDbSupport;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.StringUtils;

import java.util.Objects;

/**
 * 继承 AbstractJob 必须在类上加上 @DependsOn(value = {"scheduler", "jobDbConfig"})
 */
@DependsOn(value = "scheduler")
@Lazy
public abstract class AbstractJob implements InitializingBean
{
    @Getter
    @Setter
    private Job job;

    protected abstract Runnable work();

    protected abstract String getJobName();

    protected boolean canWork()
    {
        job = getCurrentJob();
        return Objects.nonNull(job);
    }

    @Override
    public void afterPropertiesSet()
    {
        registerJob();
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
        Jobs.remove(job.getId());
        job = getCurrentJob();
        if (Objects.nonNull(job))
        {
            Jobs.add(job);
        }
    }

    private Job getCurrentJob()
    {
        var entitys = JobDbSupport.query("select * from " + JobDbSupport.getTableName() + " where job_name=?", new Object[]{getJobName()});
        if (CollectionUtils.isEmpty(entitys) || entitys.size() > 1)
        {
            return null;
        }
        var entity = entitys.get(0);
        if (entity.getStatus() == -1 || !entity.getEnabled() || StringUtils.isBlank(entity.getCron()))
        {
            return null;
        }
        return Job.builder()
                .id(entity.getId())
                .cron(entity.getCron())
                .jobName(entity.getJobName())
                .status(entity.getStatus())
                .work(work())
                .build();
    }
}
