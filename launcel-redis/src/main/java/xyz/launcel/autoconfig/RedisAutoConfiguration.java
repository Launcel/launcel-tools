package xyz.launcel.autoconfig;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyz.launcel.lang.Base64;
import xyz.launcel.log.RootLogger;
import xyz.launcel.properties.RedisProperties;
import xyz.launcel.support.serializer.GsonRedisSerializer;

import javax.inject.Named;
import java.time.Duration;


@EnableCaching
@Configuration
@EnableConfigurationProperties(value = RedisProperties.class)
public class RedisAutoConfiguration extends CachingConfigurerSupport
{

    private final RedisProperties properties;

    public RedisAutoConfiguration(RedisProperties redisProperties)
    {
        this.properties = redisProperties;
    }

    private GenericObjectPoolConfig genericObjectPoolConfig()
    {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(properties.getMaxIdle());
        poolConfig.setMaxTotal(properties.getMaxTotal());
        poolConfig.setMinIdle(properties.getMinIdle());
        poolConfig.setMaxWaitMillis(properties.getMaxWait());
        return poolConfig;
    }

    private RedisStandaloneConfiguration sinagleConfiguration()
    {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(properties.getDatabase());
        redisStandaloneConfiguration.setHostName(properties.getHost());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(Base64.decode(properties.getPassword())));
        redisStandaloneConfiguration.setPort(properties.getPort());
        return redisStandaloneConfiguration;
    }

    private LettucePoolingClientConfiguration lettucePoolClient()
    {
        return LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(properties.getTimeout()))
                .poolConfig(genericObjectPoolConfig())
                .build();
    }

    @Primary
    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory lettuceConnectionFactory()
    {
        return new LettuceConnectionFactory(sinagleConfiguration(), lettucePoolClient());
    }


    @Primary
    @Bean(name = "redisTemplate")
    @ConditionalOnBean(name = "redisConnectionFactory")
    public RedisTemplate<String, Object> redisTemplate(@Named("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory)
    {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //                FastJsonRedisSerializer<?> serializer            = new FastJsonRedisSerializer<>(Object.class);
        GsonRedisSerializer<?> serializer            = new GsonRedisSerializer<>(Object.class);
        StringRedisSerializer  stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        try
        {
            Class<?>        clazz                = Class.forName(properties.getValueSerializer());
            RedisSerializer redisValueSerializer = (RedisSerializer) clazz.getDeclaredConstructor().newInstance();
            template.setValueSerializer(redisValueSerializer);
        }
        catch (ReflectiveOperationException e)
        {
            RootLogger.error("redis value serializer init error, there will be use jdk serializer");
            template.setValueSerializer(new JdkSerializationRedisSerializer());
        }
        template.setDefaultSerializer(serializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Primary
    @Bean
    public CacheManager cacheManager(@Named("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory)
    {
        // 设置缓存有效期一小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler()
    {
        return new CacheErrorHandler()
        {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key)
            {
                RootLogger.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value)
            {
                RootLogger.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)
            {
                RootLogger.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache)
            {
                RootLogger.error("redis异常：", e.getMessage());
            }
        };
    }

}
