package xyz.launcel.autoconfig;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.job.config.JobDbConfig;
import xyz.launcel.job.config.impl.CacheJobDbConfig;
import xyz.launcel.job.context.Jobs;
import xyz.launcel.properties.JobDatasourceProperties;
import xyz.launcel.properties.SchedulePoolProperties;
import xyz.launcel.properties.ThreadPoolProperties;

/**
 * Created by launcel on 2018/8/6.
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(value = {ThreadPoolProperties.class, SchedulePoolProperties.class, JobDatasourceProperties.class})
@RequiredArgsConstructor
public class CommonAutoConfiguration implements ApplicationContextAware, InitializingBean
{
    private final ThreadPoolProperties    threadPoolProperties;
    private final SchedulePoolProperties  schedulerPoolProperties;
    private final JobDatasourceProperties jobDatasourceProperties;

    @Bean(name = "executor")
    @Primary
    @ConditionalOnProperty(prefix = "thread.pool", value = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor taskExecutor()
    {
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
    public ThreadPoolTaskScheduler taskScheduler()
    {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerPoolProperties.getPoolSize());
        scheduler.setThreadGroupName("taskScheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(300);
        Jobs.setScheduler(scheduler);
        return scheduler;
    }

    @Bean(name = "jobDbConfig")
    @Primary
    @ConditionalOnProperty(prefix = "job.datasource", value = "enabled", havingValue = "true")
    public JobDbConfig jobDbConfig()
    {
        var jobDbConfig = new CacheJobDbConfig();
        jobDbConfig.setTableName(jobDatasourceProperties.getTableName());
        jobDbConfig.setDriverClass(jobDatasourceProperties.getDriverClass());
        jobDbConfig.setUrl(jobDatasourceProperties.getUrl());
        jobDbConfig.setUser(jobDatasourceProperties.getUser());
        jobDbConfig.setPassword(jobDatasourceProperties.getPassword());
        return jobDbConfig;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        SpringBeanUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet()
    {
        ExceptionHelp.initProperties();
    }
}
