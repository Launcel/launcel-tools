package xyz.x.common.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import xyz.x.common.properties.ThreadPoolProperties;
import xyz.x.log.Log;

@RequiredArgsConstructor
@EnableAsync
@Configuration
@EnableConfigurationProperties(value = {ThreadPoolProperties.class})
public class ThreadPoolConfiguration
{
    private final ThreadPoolProperties threadPoolProperties;

    @Bean(name = "executor")
    @Primary
    public ThreadPoolTaskExecutor executor()
    {
        Log.info("init thread.pool.executor");
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.initialize();
        return executor;
    }
}
