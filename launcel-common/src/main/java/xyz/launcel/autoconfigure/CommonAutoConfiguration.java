package xyz.launcel.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.job.config.JobDbConfig;
import xyz.launcel.job.config.impl.CacheJobDbProperties;
import xyz.launcel.properties.SchedulerPoolProperties;
import xyz.launcel.properties.ThreadPoolProperties;

/**
 * Created by launcel on 2018/8/6.
 */
//@EnableAsync
@Configuration
@EnableConfigurationProperties(value = {ThreadPoolProperties.class, SchedulerPoolProperties.class, CacheJobDbProperties.class})
public class CommonAutoConfiguration implements ApplicationContextAware, InitializingBean
{
    private final ThreadPoolProperties    threadPoolProperties;
    private final SchedulerPoolProperties schedulerPoolProperties;
    private final CacheJobDbProperties    cacheJobDbProperties;

    public CommonAutoConfiguration(ThreadPoolProperties threadPoolProperties, SchedulerPoolProperties schedulerPoolProperties,
                                   CacheJobDbProperties cacheJobDbProperties)
    {
        System.out.println("init CommonAutoConfiguration....");
        this.threadPoolProperties = threadPoolProperties;
        this.schedulerPoolProperties = schedulerPoolProperties;
        this.cacheJobDbProperties = cacheJobDbProperties;
    }

    @Bean(name = "executor")
    @Primary
    @ConditionalOnProperty(prefix = "thread.pool", value = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor executor()
    {
        System.out.println("init thread.pool.executor");
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.initialize();
        return executor;
    }

    @Bean(name = "scheduler")
    @Primary
    @ConditionalOnProperty(prefix = "job.scheduler", value = "enabled", havingValue = "true")
    public ThreadPoolTaskScheduler scheduler()
    {
        System.out.println("init job.scheduler.scheduler");
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerPoolProperties.getPoolSize());
        scheduler.setThreadGroupName("taskScheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(300);
        return scheduler;
    }

    @Bean(name = "jobDbConfig")
    @Primary
    @ConditionalOnProperty(prefix = "job.datasource", value = "enabled", havingValue = "true")
    public JobDbConfig jobDbConfig()
    {
        System.out.println("init job.datasource.jobDbConfig");
        return cacheJobDbProperties;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        System.out.println("exectute Spring setApplicationContext...");
        SpringBeanUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet()
    {
        System.out.println("exectute Spring afterPropertiesSet...");
        ExceptionHelp.initProperties();
    }
}

