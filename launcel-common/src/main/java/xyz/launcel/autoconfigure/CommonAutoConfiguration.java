package xyz.launcel.autoconfigure;

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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.job.config.JobDbConfig;
import xyz.launcel.job.config.impl.CacheJobDbConfig;
import xyz.launcel.properties.JobDatasourceProperties;
import xyz.launcel.properties.SchedulerPoolProperties;
import xyz.launcel.properties.ThreadPoolProperties;

/**
 * Created by launcel on 2018/8/6.
 */
//@EnableAsync
@Configuration
@EnableConfigurationProperties(value = {ThreadPoolProperties.class, SchedulerPoolProperties.class, JobDatasourceProperties.class})
public class CommonAutoConfiguration implements ApplicationContextAware, InitializingBean
{
    private final ThreadPoolProperties    threadPoolProperties;
    private final SchedulerPoolProperties schedulerPoolProperties;
    private final JobDatasourceProperties jobDatasourceProperties;

    @Bean(name = "executor")
    @Primary
    @ConditionalOnProperty(prefix = "thread.pool", value = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor executor()
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
    public ThreadPoolTaskScheduler scheduler()
    {
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

    public CommonAutoConfiguration(
            ThreadPoolProperties threadPoolProperties, SchedulerPoolProperties schedulerPoolProperties, JobDatasourceProperties jobDatasourceProperties)
    {
        this.threadPoolProperties = threadPoolProperties;
        this.schedulerPoolProperties = schedulerPoolProperties;
        this.jobDatasourceProperties = jobDatasourceProperties;
        System.out.println("init CommonAutoConfiguration....");
    }
}

