package xyz.launcel.job.context;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.ensure.Me;
import xyz.launcel.job.bean.Job;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class Jobs
{

    private static final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap   = new ConcurrentHashMap<>(8);
    private static       ThreadPoolTaskScheduler                     scheduler = null;

    public static void add(@NonNull Job job)
    {
        var future = getFuture(job);
        Me.builder(future).isNull("0001");
        jobsMap.put(job.getId(), future);
    }

    public static void remove(@NonNull Integer jobId)
    {
        var future = jobsMap.get(jobId);
        Me.builder(future).isNull("0002");
        future.cancel(true);
        jobsMap.remove(jobId);
    }

    public static ScheduledFuture getFuture(@NonNull Job job)
    {
        return getScheduler().schedule(job.getWork(), new CronTrigger(job.getCron()));
    }

    @NonNull
    public static ThreadPoolTaskScheduler getScheduler()
    {
        if (Jobs.scheduler == null)
        {
            synchronized (Jobs.class)
            {
                if (Jobs.scheduler == null)
                {
                    Jobs.scheduler = SpringBeanUtil.getBean("scheduler");
                }
            }
        }
        return Jobs.scheduler;
    }
}
