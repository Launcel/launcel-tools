package xyz.launcel.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePool;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import xyz.launcel.lang.Base64;
import xyz.launcel.log.RootLogger;
import xyz.launcel.properties.RedisProperties;
import xyz.launcel.support.serializer.GsonRedisSerializer;

import javax.inject.Named;

@EnableCaching
@Configuration
@EnableConfigurationProperties(value = RedisProperties.class)
public class RedisAutoConfiguration extends CachingConfigurerSupport {
    
    private final RedisProperties properties;
    
    public RedisAutoConfiguration(RedisProperties redisProperties) {
        this.properties = redisProperties;
    }
    
    private LettucePool lettucePool() {
        DefaultLettucePool lettucePool = new DefaultLettucePool();
        lettucePool.setDatabase(properties.getDatabase());
        lettucePool.setHostName(properties.getHost());
        lettucePool.setPort(properties.getPort());
        lettucePool.setPassword(Base64.decode(properties.getPassword()));
        lettucePool.setTimeout(properties.getTimeout());
        return lettucePool;
    }
    
    @Primary
    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(lettucePool());
    }
    
    
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig pool = new JedisPoolConfig();
        pool.setMinIdle(properties.getMinIdle());
        pool.setMaxIdle(properties.getMaxIdle());
        pool.setMaxTotal(properties.getMaxTotal());
        pool.setMaxWaitMillis(properties.getMaxWait());
        return pool;
    }
    
    
    @Bean(name = "redisConnectionFactory")
    @ConditionalOnMissingBean(name = "redisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setDatabase(properties.getDatabase());
        factory.setHostName(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setPassword(Base64.decode(properties.getPassword()));
        factory.setTimeout(properties.getTimeout());
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig());
        return factory;
    }
    
    @Primary
    @Bean(name = "redisTemplate")
    @ConditionalOnBean(name = "redisConnectionFactory")
    RedisTemplate<String, Object> redisTemplate(@Named("redisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        GsonRedisSerializer<Object> serializer = new GsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setDefaultSerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        
        return template;
    }
    
    @Primary
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }
    
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                RootLogger.ERROR("redis异常：key=[{}]", key.toString());
            }
            
            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                RootLogger.ERROR("redis异常：key=[{}]", key.toString());
            }
            
            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                RootLogger.ERROR("redis异常：key=[{}]", key.toString());
            }
            
            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                RootLogger.ERROR("redis异常：", e.getMessage());
            }
        };
    }
    
}
