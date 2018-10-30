package xyz.launcel.autoconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import xyz.launcel.properties.ThreadPoolProperties;

/**
 * Created by launcel on 2018/8/6.
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(value = {ThreadPoolProperties.class})
@RequiredArgsConstructor
public class AsyncTaskAutoConfiguration
{
    private final ThreadPoolProperties poolProperties;

    @Bean(name = "executor")
    public ThreadPoolTaskExecutor taskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolProperties.getCorePoolSize());
        executor.setMaxPoolSize(poolProperties.getMaxPoolSize());
        executor.setQueueCapacity(poolProperties.getQueueCapacity());
        executor.initialize();
        return executor;

    }
}
