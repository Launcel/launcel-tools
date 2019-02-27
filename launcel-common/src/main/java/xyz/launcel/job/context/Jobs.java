package xyz.launcel.job.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import xyz.launcel.ensure.Me;
import xyz.launcel.job.AbstractJob;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jobs
{
    //    private static final ThreadPoolTaskScheduler scheduler = SpringBeanUtil.getBean(ThreadPoolTaskScheduler.class);
    @Inject
    @Named("scheduler")
    private static ThreadPoolTaskScheduler scheduler;

    private static final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public static void add(AbstractJob.Job job, Runnable r)
    {
        ScheduledFuture future = execute(r, job.getCron());
        Me.builder(future).isNull("");
        jobsMap.put(job.getId(), future);
    }

    public static void stop(Integer jobId)
    {
        var future = jobsMap.get(jobId);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.remove(jobId);
        }
    }

    public static void reset(AbstractJob.Job job, Runnable r)
    {
        var future = jobsMap.get(job.getId());
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.replace(job.getId(), execute(r, job.getCron()));
        }
    }

    private static ScheduledFuture execute(Runnable r, String cron)
    {
        System.out.println("scheduler == " + scheduler.toString());
        return scheduler.schedule(r, new CronTrigger(cron));
    }
}
