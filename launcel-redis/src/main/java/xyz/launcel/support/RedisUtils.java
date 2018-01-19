package xyz.launcel.support;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisCommands;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.ApplicationContextHook;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.prop.RedisProperties;

import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private RedisUtils() {
    }

    @SuppressWarnings("unchecked")
    private static RedisTemplate<String, Object> template = (RedisTemplate<String, Object>) ApplicationContextHook.getBean("redisTemplate");

    private static Long expTime = ((RedisProperties) ApplicationContextHook.getBean(RedisProperties.class)).getExpTime();

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
        return set(key, value, expTime);
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

    public static Boolean setIfAbsent(final String key, final String value, final Long expTime) {
        vidate(key, value);
        String str = template.execute((RedisCallback<String>) connection -> {
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            return commands.set(key, value, "NX", "PX", expTime);
        });
        return StringUtils.isBlank(str);
    }

    public static Object getAndSet(final String key, final Object value) {
        vidate(value);
        if (!exits(key))
            ExceptionFactory.create("_REDIS__ERROR_CODE_004");
        return template.opsForValue().getAndSet(key, value);
    }

    public static Long getExpTime(final String key) {
        vidate(key);
        return template.getExpire(key);
    }

    private static void vidate(final String key) {
        Me.that(key).isBlank("_REDIS__ERROR_CODE_001");
    }

    private static void vidate(final Object value) {
        Me.that(value).isNull("_REDIS__ERROR_CODE_002");
    }

    private static void viate(final Long expTime) {
        Me.that(expTime == 0).isTrue("_REDIS__ERROR_CODE_003");
    }

    private static void vidate(final String key, final Object value) {
        vidate(key);
        vidate(value);
    }

    private static void vidate(final String key, final Long expTime) {
        vidate(key);
        vidate(expTime);
    }

    private static void vidate(final String key, final Object value, final Long expTime) {
        vidate(key);
        vidate(expTime);
        vidate(value);
    }


}
