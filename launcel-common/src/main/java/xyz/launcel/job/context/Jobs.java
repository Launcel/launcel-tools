package xyz.launcel.job.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class Jobs
{
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final ConcurrentHashMap<String, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public void start(String jobName)
    {
        if (jobsMap.containsKey(jobName))
        {
            return;
        }
    }

    public void stop(String jobName)
    {
        ScheduledFuture<?> future = jobsMap.get(jobName);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
        }
    }

    private void execute()
    {

    }
}
