package xyz.launcel.job.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jobs
{

    private static final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public static void add(@NonNull Integer jobId, @NonNull ScheduledFuture job)
    {
        jobsMap.put(jobId, job);
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

    public static void reset(@NonNull Integer jobId, @NonNull ScheduledFuture job)
    {
        var future = jobsMap.get(jobId);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
            jobsMap.replace(jobId, job);
        }
    }
}
