package xyz.launcel.support;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisCommands;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.ApplicationContextHook;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.properties.RedisProperties;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <b>普通情况下，建议用RedisConnection(RedisTemplate是基于RedisConnection) 操作，pipeline性能高，</b><br/>
 * 控制并发数据时，建议用JedisCommands(直接适用Jedis),见setNX方法<b>(但只能处理String类型)</b>
 */
public class RedisUtils {
    
    private static RedisTemplate<String, Object> template   = ApplicationContextHook.getBean("redisTemplate");
    private static Long                          expireTime = ApplicationContextHook.getBean(RedisProperties.class).getExptime();
    
    public static RedisTemplate<String, Object> getTemplate() {
        return template;
    }
    
    private static Long getExpireTime() {
        return expireTime;
    }
    
    public static void setExpireTime(Long expireTime) {
        RedisUtils.expireTime = expireTime;
    }
    
    private RedisUtils() {
    }

    public static void remove(final String... key) {
        if (key.length <= 0) {
            ExceptionFactory.create("_REDIS__ERROR_CODE_001", "redis key is null");
        }
        for (String str : key) {
            remove(str);
        }
    }
    
    public static void remove(final String key) {
        if (exits(key)) {
            getTemplate().delete(key);
        } else {
            ExceptionFactory.create("_REDIS__ERROR_CODE_004", "redis cannot find the value of this key");
        }
    }

    public static void bentchRemove(final String pattern) {
        getTemplate().execute((RedisCallback<Long>) connection -> getCommands(connection).del(pattern));
    }

    public static boolean exits(final String key) {
        vidate(key);
        return getTemplate().hasKey(key);
    }
    
    public static Object get(final String key) {
        vidate(key);
        return getTemplate().opsForValue().get(key);
    }
    
    public static boolean set(final String key, final Object value) {
        return set(key, value, getExpireTime());
    }
    
    public static boolean set(final String key, final Object value, final Long expTime) {
        return set(key, value, expTime, TimeUnit.SECONDS);
    }
    
    public static boolean set(final String key, final Object value, final Long expTime, TimeUnit timeUnit) {
        try {
            vidate(key, value, expTime);
            getTemplate().opsForValue().set(key, value, expTime, timeUnit);
            return true;
        } catch (Exception x) {
            return false;
        }
        
    }
    
    private static JedisCommands getCommands(RedisConnection connection) {
        return (JedisCommands) connection.getNativeConnection();
    }
    
    public static Boolean setNX(final String key, final String value, final Long expTime) {
        vidate(key, value);
        String str = getTemplate().execute((RedisCallback<String>) connection -> getCommands(connection).set(key, value, "NX", "PX", expTime));
        return StringUtils.isBlank(str);
    }
    
    /**
     * RedisTemplate set方法默认使用的是jedis的setEX
     *
     */
    public static String setEX(final String key, String value) {
        return setEX(key, value, getExpireTime());
    }
    
    /**
     * RedisTemplate set方法默认使用的是jedis的setEX
     *
     */
    public static String setEX(final String key, final String value, final Long expTime) {
        vidate(key, value, expTime);
        return getTemplate().execute((RedisCallback<String>) connection -> getCommands(connection).setex(key, expTime.intValue(), value));
    }
    
    public static Object getAndSet(final String key, final Object value) {
        vidate(value);
        if (!exits(key)) {
            ExceptionFactory.create("_REDIS__ERROR_CODE_004", "redis cannot find the value of this key");
        }
//        return template.execute((RedisCallback<String>) connection ->
//                Arrays.toString(connection.getSet(SafeEncoder.encode(key), SafeEncoder.encode(Json.toJson(value)))));
        return getTemplate().opsForValue().getAndSet(key, value);
    }
    
    public static Long getExpTime(final String key) {
        vidate(key);
        return getTemplate().getExpire(key);
    }
    
    private static void vidate(final Object value) {
        vidate("1", value);
    }
    
    private static void vidate(final String key) {
        vidate(key, true);
    }
    
    private static void vidate(final String key, final Object value) {
        vidate(key, value, 1L);
    }
    
    
    private static void vidate(final String key, final Long expTime) {
        vidate(key, true, 1L);
    }
    
    private static void vidate(final String key, final Object value, final Long expTime) {
        if (StringUtils.isBlank(key)) {
            ExceptionFactory.create("_REDIS__ERROR_CODE_001", "redis key is null");
        }
        if (Objects.isNull(value)) {
            ExceptionFactory.create("_REDIS__ERROR_CODE_002", "redis value is null");
        }
        if (expTime == null || expTime <= 0) {
            ExceptionFactory.create("_REDIS__ERROR_CODE_003", "redis expTime is error");
        }
    }
    
    
}
