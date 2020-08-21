//package xyz.x.common.autoconfigure;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.lang.NonNull;
//import xyz.x.common.SpringBeanUtil;
//import xyz.x.common.exception.ExceptionHelp;
//import xyz.x.log.Log;
//
///**
// * Created by launcel on 2018/8/6.
// */
//@EnableAsync
//@Configuration
//@EnableConfigurationProperties(value = {ThreadPoolProperties.class, SchedulerPoolProperties.class, CacheJobDbProperties.class})
//public class CommonAutoConfiguration implements ApplicationContextAware, InitializingBean
//{
//        private final ThreadPoolProperties    threadPoolProperties;
//        private final SchedulerPoolProperties schedulerPoolProperties;
//        private final CacheJobDbProperties    cacheJobDbProperties;
//
//        public CommonAutoConfiguration(ThreadPoolProperties threadPoolProperties, SchedulerPoolProperties schedulerPoolProperties,
//                                       CacheJobDbProperties cacheJobDbProperties)
//        {
//            Log.info("init CommonAutoConfiguration....");
//            this.threadPoolProperties = threadPoolProperties;
//            this.schedulerPoolProperties = schedulerPoolProperties;
//            this.cacheJobDbProperties = cacheJobDbProperties;
//        }
//
//        @Bean(name = "executor")
//        @Primary
//        @ConditionalOnProperty(prefix = "thread.pool", value = "enabled", havingValue = "true")
//        public ThreadPoolTaskExecutor executor()
//        {
//            Log.info("init thread.pool.executor");
//            var executor = new ThreadPoolTaskExecutor();
//            executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
//            executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
//            executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
//            executor.initialize();
//            return executor;
//        }
//
//        @Bean(name = "scheduler")
//        @Primary
//        @ConditionalOnProperty(prefix = "job.scheduler", value = "enabled", havingValue = "true")
//        public ThreadPoolTaskScheduler scheduler()
//        {
//            Log.info("init job.scheduler.scheduler");
//            var scheduler = new ThreadPoolTaskScheduler();
//            scheduler.setPoolSize(schedulerPoolProperties.getPoolSize());
//            scheduler.setThreadGroupName("taskScheduler-");
//            scheduler.setWaitForTasksToCompleteOnShutdown(true);
//            scheduler.setAwaitTerminationSeconds(300);
//            return scheduler;
//        }
//
//        @Bean(name = "jobDbConfig")
//        @Primary
//        @ConditionalOnProperty(prefix = "job.datasource", value = "enabled", havingValue = "true")
//        public JobDbConfig jobDbConfig()
//        {
//            Log.info("init job.datasource.jobDbConfig");
//            return cacheJobDbProperties;
//        }
//
//    @Override
//    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
//    {
//        Log.info("exectute Spring setApplicationContext...");
//        SpringBeanUtil.setApplicationContext(applicationContext);
//    }
//
//    @Override
//    public void afterPropertiesSet()
//    {
//        Log.info("exectute Spring afterPropertiesSet...");
//        ExceptionHelp.initProperties();
//    }
//}
//
