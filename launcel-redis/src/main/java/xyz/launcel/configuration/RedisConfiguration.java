package xyz.launcel.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import xyz.launcel.lang.Base64;
import xyz.launcel.prop.RedisConfigProp;
import xyz.launcel.support.serializer.GsonRedisSerializer;

import javax.inject.Inject;

@EnableCaching
@Configuration
@EnableConfigurationProperties(value = RedisConfigProp.class)
public class RedisConfiguration extends CachingConfigurerSupport {

    private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    @Inject
    private RedisConfigProp redisConf;

    @Primary
    @Bean(name = "redisPool")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig pool = new JedisPoolConfig();
        pool.setMinIdle(redisConf.getMinIdle());
        pool.setMaxIdle(redisConf.getMaxIdle());
        pool.setMaxWaitMillis(redisConf.getMaxWait());
        return pool;
    }

    @Primary
    @Bean(name = "redisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier("redisPool") JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setDatabase(redisConf.getDatabase());
        factory.setHostName(redisConf.getHost());
        factory.setPort(redisConf.getPort());
        factory.setPassword(Base64.decode(redisConf.getPassword()));
        factory.setTimeout(300);
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig);
        return factory;
    }

    @Primary
    @Bean(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
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
                logger.error("redis异常：key=[{}]", key);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                logger.error("redis异常：key=[{}]", key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                logger.error("redis异常：key=[{}]", key);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                logger.error("redis异常：", e);
            }
        };
    }

}
