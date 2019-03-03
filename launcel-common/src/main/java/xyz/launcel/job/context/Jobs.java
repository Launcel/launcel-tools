package xyz.launcel.job.context;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import xyz.launcel.job.AbstractJob;

import javax.inject.Named;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class Jobs
{
    private static ThreadPoolTaskScheduler scheduler;

    @Autowired
    public void setScheduler(@Named("scheduler") ThreadPoolTaskScheduler scheduler)
    {
        Jobs.scheduler = scheduler;
    }

    private static final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public static void add(@NonNull AbstractJob.Job job)
    {
        var future = getFuture(job);
        if (Objects.nonNull(future))
        {
            jobsMap.put(job.getId(), future);
        }
    }

    public static void remove(@NonNull Integer jobId)
    {
        var future = jobsMap.get(jobId);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.remove(jobId);
        }
    }

    public static ScheduledFuture getFuture(@NonNull AbstractJob.Job job)
    {
        return scheduler.schedule(job.getWork(), new CronTrigger(job.getCron()));
    }
}
