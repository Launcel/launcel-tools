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

    private final ConcurrentHashMap<Integer, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(8);

    public void start(Integer jid)
    {
        if (jobsMap.containsKey(jid))
        {
            return;
        }
    }

    public void stop(Integer jid)
    {
        ScheduledFuture<?> future = jobsMap.get(jid);
        if (Objects.nonNull(future))
        {
            future.cancel(true);
        }
    }

    private void execute()
    {

    }
}
