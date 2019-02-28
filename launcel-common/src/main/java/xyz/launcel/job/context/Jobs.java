package xyz.launcel.job.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.ensure.Me;
import xyz.launcel.job.AbstractJob;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jobs
{
    private static ThreadPoolTaskScheduler scheduler;

    private static final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public static void add(AbstractJob.Job job, Runnable r)
    {
        var future = execute(r, job.getCron());
        Me.builder(future).isNull("create job error!!!");
        jobsMap.put(job.getId(), future);
    }

    public static void stop(@NonNull Integer jobId)
    {
        var future = jobsMap.get(jobId);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.remove(jobId);
        }
    }

    public static void reset(@NonNull AbstractJob.Job job, @NonNull Runnable r)
    {
        var future = jobsMap.get(job.getId());
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.replace(job.getId(), execute(r, job.getCron()));
        }
    }

    private static ScheduledFuture execute(@NonNull Runnable r, @NonNull String cron)
    {
        if (Objects.isNull(scheduler))
        {
            synchronized (jobsMap)
            {
                if (Objects.isNull(scheduler))
                {
                    scheduler = SpringBeanUtil.getBean(ThreadPoolTaskScheduler.class);
                }
            }
        }
        return scheduler.schedule(r, new CronTrigger(cron));
    }

    public static void setScheduler(@NonNull ThreadPoolTaskScheduler schedulerPool)
    {
        if (scheduler == null)
        {
            System.out.print("init ThreadPoolTaskScheduler bean...");
            System.out.print(schedulerPool.toString());
            scheduler = schedulerPool;
        }
    }
}
