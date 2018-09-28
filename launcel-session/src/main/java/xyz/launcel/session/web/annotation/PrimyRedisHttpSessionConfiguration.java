//package xyz.launcel.session.web.annotation;
//
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.EmbeddedValueResolverAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ImportAware;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
//import org.springframework.session.data.redis.RedisFlushMode;
//import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;
//import org.springframework.session.data.redis.config.ConfigureRedisAction;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import org.springframework.util.StringValueResolver;
//import xyz.launcel.session.redis.PrimyRedisOperationsSessionRepository;
//
//import javax.inject.Named;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Map;
//import java.util.concurrent.Executor;
//
//public class PrimyRedisHttpSessionConfiguration extends SpringHttpSessionConfiguration implements EmbeddedValueResolverAware, ImportAware
//{
//
//    private Integer maxInactiveIntervalInSeconds = 1200;
//
//    private ConfigureRedisAction configureRedisAction = new ConfigureNotifyKeyspaceEventsAction();
//
//    private String redisNamespace = "";
//
//    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;
//
//    private Executor redisTaskExecutor;
//
//    private Executor redisSubscriptionExecutor;
//
//    private StringValueResolver embeddedValueResolver;
//
//    @ConditionalOnBean(name = "redisConnectionFactory")
//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(@Named("redisConnectionFactory") RedisConnectionFactory connectionFactory, PrimyRedisOperationsSessionRepository messageListener)
//    {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        if (this.redisTaskExecutor != null)
//        {
//            container.setTaskExecutor(this.redisTaskExecutor);
//        }
//        if (this.redisSubscriptionExecutor != null)
//        {
//            container.setSubscriptionExecutor(this.redisSubscriptionExecutor);
//        }
//        container.addMessageListener(messageListener, Arrays.asList(new PatternTopic("__keyevent@*:del"), new PatternTopic("__keyevent@*:expired")));
//        container.addMessageListener(messageListener, Collections.singletonList(new PatternTopic(messageListener.getSessionCreatedChannelPrefix() + "*")));
//        return container;
//    }
//
//    @ConditionalOnBean(name = "redisTemplate")
//    @Bean
//    public PrimyRedisOperationsSessionRepository sessionRepository(@Named("redisTemplate") RedisOperations<String, Object> sessionRedisTemplate, ApplicationEventPublisher applicationEventPublisher)
//    {
//        PrimyRedisOperationsSessionRepository sessionRepository = new PrimyRedisOperationsSessionRepository(sessionRedisTemplate);
//        sessionRepository.setApplicationEventPublisher(applicationEventPublisher);
//        sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);
//        String redisNamespace = getRedisNamespace();
//        if (StringUtils.hasText(redisNamespace))
//        {
//            sessionRepository.setRedisKeyNamespace(redisNamespace);
//        }
//
//        sessionRepository.setRedisFlushMode(this.redisFlushMode);
//        return sessionRepository;
//    }
//
//    public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds)
//    {
//        this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
//    }
//
//    public void setRedisNamespace(String namespace)
//    {
//        this.redisNamespace = namespace;
//    }
//
//    public void setRedisFlushMode(RedisFlushMode redisFlushMode)
//    {
//        Assert.notNull(redisFlushMode, "redisFlushMode cannot be null");
//        this.redisFlushMode = redisFlushMode;
//    }
//
//    private String getRedisNamespace()
//    {
//        if (StringUtils.hasText(this.redisNamespace))
//        {
//            return this.redisNamespace;
//        }
//        return System.getProperty("spring.session.redis.namespace", "");
//    }
//
//    public void setImportMetadata(AnnotationMetadata importMetadata)
//    {
//        Map<String, Object>  enableAttrMap = importMetadata.getAnnotationAttributes(PrimyEnableRedisHttpSession.class.getName());
//        AnnotationAttributes enableAttrs   = AnnotationAttributes.fromMap(enableAttrMap);
//        setMaxInactiveIntervalInSeconds(enableAttrs.getNumber("maxInactiveIntervalInSeconds"));
//        String redisNamespaceValue = enableAttrs.getString("redisNamespace");
//        if (StringUtils.hasText(redisNamespaceValue))
//        {
//            this.redisNamespace = this.embeddedValueResolver.resolveStringValue(redisNamespaceValue);
//        }
//        this.redisFlushMode = enableAttrs.getEnum("redisFlushMode");
//    }
//
//    @ConditionalOnBean(name = "redisConnectionFactory")
//    @Bean
//    public InitializingBean enableRedisKeyspaceNotificationsInitializer(@Named(value = "redisConnectionFactory") RedisConnectionFactory connectionFactory)
//    {
//        return new EnableRedisKeyspaceNotificationsInitializer(connectionFactory, this.configureRedisAction);
//    }
//
//    @Autowired(required = false)
//    public void setConfigureRedisAction(ConfigureRedisAction configureRedisAction)
//    {
//        this.configureRedisAction = configureRedisAction;
//    }
//
//    //    @Autowired(required = false)
//    //    @Qualifier("springSessionDefaultRedisSerializer")
//    //    public void setDefaultRedisSerializer(RedisSerializer<Object> defaultRedisSerializer) {
//    //    }
//
//    @Autowired(required = false)
//    @Named("springSessionRedisTaskExecutor")
//    public void setRedisTaskExecutor(Executor redisTaskExecutor)
//    {
//        this.redisTaskExecutor = redisTaskExecutor;
//    }
//
//    @Autowired(required = false)
//    @Named("springSessionRedisSubscriptionExecutor")
//    public void setRedisSubscriptionExecutor(Executor redisSubscriptionExecutor)
//    {
//        this.redisSubscriptionExecutor = redisSubscriptionExecutor;
//    }
//
//    public void setEmbeddedValueResolver(StringValueResolver resolver)
//    {
//        embeddedValueResolver = resolver;
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
//    {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    static class EnableRedisKeyspaceNotificationsInitializer implements InitializingBean
//    {
//        private final RedisConnectionFactory connectionFactory;
//
//        private ConfigureRedisAction configure;
//
//        EnableRedisKeyspaceNotificationsInitializer(RedisConnectionFactory connectionFactory, ConfigureRedisAction configure)
//        {
//            this.connectionFactory = connectionFactory;
//            this.configure = configure;
//        }
//
//        public void afterPropertiesSet()
//        {
//            if (this.configure == ConfigureRedisAction.NO_OP)
//            {
//                return;
//            }
//            RedisConnection connection = this.connectionFactory.getConnection();
//            try
//            {
//                this.configure.configure(connection);
//            }
//            finally
//            {
//                try
//                {
//                    connection.close();
//                }
//                catch (Exception e)
//                {
//                    LogFactory.getLog(getClass()).error("Error closing RedisConnection", e);
//                }
//            }
//        }
//    }
//}
