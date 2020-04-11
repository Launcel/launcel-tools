package xyz.launcel.redis.autoconfigure;

import lombok.var;
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
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import xyz.launcel.redis.core.RedisOperation;
import xyz.launcel.redis.core.RedisTemplates;
import xyz.launcel.log.Log;
import xyz.launcel.redis.properties.RedisProperties;
import xyz.launcel.redis.support.serializer.KeyRedisSerializer;
import xyz.launcel.utils.Base64;

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
        Log.info("init RedisAutoConfiguration....");
        this.properties = redisProperties;
    }

    private GenericObjectPoolConfig genericObjectPoolConfig()
    {
        var poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(properties.getMaxIdle());
        poolConfig.setMaxTotal(properties.getMaxTotal());
        poolConfig.setMinIdle(properties.getMinIdle());
        poolConfig.setMaxWaitMillis(properties.getMaxWait());
        return poolConfig;
    }

    private RedisStandaloneConfiguration sinagleConfiguration()
    {
        var redisStandaloneConfiguration = new RedisStandaloneConfiguration();
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
        Log.info("init redisConnectionFactory...");
        return new LettuceConnectionFactory(sinagleConfiguration(), lettucePoolClient());
    }

    @Primary
    @Bean(name = "redisOperation")
    @ConditionalOnBean(name = "redisConnectionFactory")
    public RedisOperation redisOperation(@NonNull @Named("redisConnectionFactory") final RedisConnectionFactory redisConnectionFactory)
    {
        Log.info("init redisTemplate...");
        var template = new RedisTemplates();
        template.setConnectionFactory(redisConnectionFactory);
        try
        {
            var clazz           = Class.forName(properties.getValueSerializer());
            var valueSerializer = (RedisSerializer) clazz.getDeclaredConstructor().newInstance();
            template.setValueSerializer(valueSerializer);

            var hasKeyClazz      = Class.forName(properties.getHashKeySerializer());
            var hasKeySerializer = (RedisSerializer) hasKeyClazz.getDeclaredConstructor().newInstance();
            template.setHashKeySerializer(hasKeySerializer);

            var keySerializer = new KeyRedisSerializer();
            template.setKeySerializer(keySerializer);


        }
        catch (ReflectiveOperationException e)
        {
            Log.warn("redis value serializer init error, there will be use jdk serializer");
        }
        return template;
    }

    @Primary
    @Bean(value = "cacheManager")
    public CacheManager cacheManager(@NonNull @Named("redisConnectionFactory") final RedisConnectionFactory redisConnectionFactory)
    {
        Log.info("init cacheManager...");
        // 设置缓存有效期一小时
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)).cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(value = "errorHandler")
    @Override
    public CacheErrorHandler errorHandler()
    {
        Log.info("init errorHandler...");
        return new CacheErrorHandler()
        {
            @Override
            public void handleCacheGetError(@NonNull RuntimeException e, @NonNull Cache cache, @NonNull Object key)
            {
                Log.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCachePutError(@NonNull RuntimeException e, @NonNull Cache cache, @NonNull Object key, Object value)
            {
                Log.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCacheEvictError(@NonNull RuntimeException e, @NonNull Cache cache, @NonNull Object key)
            {
                Log.error("redis异常：key=[{}]", key.toString());
            }

            @Override
            public void handleCacheClearError(@NonNull RuntimeException e, @NonNull Cache cache)
            {
                Log.error("redis异常：", e.getMessage());
            }
        };
    }
}
