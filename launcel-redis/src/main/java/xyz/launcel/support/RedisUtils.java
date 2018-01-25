package xyz.launcel.support;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisCommands;
import redis.clients.util.SafeEncoder;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.ApplicationContextHook;
import xyz.launcel.lang.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.prop.RedisProperties;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <b>普通情况下，建议用RedisConnection(RedisTemplate是基于RedisConnection) 操作，pipeline性能高，</b><br/>
 * 控制并发数据时，建议用JedisCommands(直接适用Jedis),见setNX方法<b>(但只能处理String类型)</b>
 */
public class RedisUtils {

    private RedisUtils() {
    }

    @SuppressWarnings("unchecked")
    private static RedisTemplate<String, Object> template = (RedisTemplate<String, Object>) ApplicationContextHook.getBean("redisTemplate");

    private static Long expireTime = ((RedisProperties) ApplicationContextHook.getBean(RedisProperties.class)).getExpTime();

    public static RedisTemplate<String, Object> getTemplate() {
        return template;
    }

    public static void remove(final String... key) {
        Me.that(key.length > 0).isFalse("_REDIS__ERROR_CODE_001");
        for (String str : key)
            remove(str);
    }

    public static void remove(final String key) {
        if (exits(key))
            template.delete(key);
        else
            ExceptionFactory.create("_REDIS__ERROR_CODE_004");
    }

    public static boolean exits(final String key) {
        vidate(key);
        return template.hasKey(key);
    }

    public static Object get(final String key) {
        vidate(key);
        return template.opsForValue().get(key);
    }

    public static boolean set(final String key, final Object value) {
        return set(key, value, expireTime);
    }

    public static boolean set(final String key, final Object value, final Long expTime) {
        return set(key, value, expTime, TimeUnit.SECONDS);
    }

    public static boolean set(final String key, final Object value, final Long expTime, TimeUnit timeUnit) {
        try {
            vidate(key, value, expTime);
            template.opsForValue().set(key, value, expTime, timeUnit);
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
        String str = template.execute((RedisCallback<String>) connection -> getCommands(connection).set(key, value, "NX", "PX", expTime));
        return StringUtils.isBlank(str);
    }

    /**
     * RedisTemplate set方法默认使用的是jedis的setEX
     *
     * @param key
     * @param value
     * @return
     */
    public static String setEX(final String key, String value) {
        return setEX(key, value, expireTime);
    }

    /**
     * RedisTemplate set方法默认使用的是jedis的setEX
     *
     * @param key
     * @param value
     * @param expTime
     * @return
     */
    public static String setEX(final String key, final String value, final Long expTime) {
        vidate(key, value, expTime);
        return template.execute((RedisCallback<String>) connection -> getCommands(connection).setex(key, expTime.intValue(), value));
    }

    public static Object getAndSet(final String key, final Object value) {
        vidate(value);
        if (!exits(key))
            ExceptionFactory.create("_REDIS__ERROR_CODE_004");
//        return template.execute((RedisCallback<String>) connection ->
//                Arrays.toString(connection.getSet(SafeEncoder.encode(key), SafeEncoder.encode(Json.toJson(value)))));
        return template.opsForValue().getAndSet(key, value);
    }

    public static Long getExpTime(final String key) {
        vidate(key);
        return template.getExpire(key);
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
        vidate(key);
        Me.that(expTime == null || expTime <= 0).isTrue("_REDIS__ERROR_CODE_003");
    }

    private static void vidate(final String key, final Object value, final Long expTime) {
        Me.that(key).isBlank("_REDIS__ERROR_CODE_001");
        Me.that(value).isNull("_REDIS__ERROR_CODE_002");
        Me.that(expTime == null || expTime <= 0).isTrue("_REDIS__ERROR_CODE_003");
    }


}
