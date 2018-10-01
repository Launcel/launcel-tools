package xyz.launcel.support;

import lombok.Getter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import xyz.launcel.bean.context.SpringBeanUtil;
import xyz.launcel.exception.SystemException;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.properties.RedisProperties;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <b>普通情况下，建议用RedisConnection(RedisTemplate是基于RedisConnection) 操作，pipeline性能高，</b><br/>
 * 控制并发数据时，用Commands,见setNX方法<b>(但只能处理String类型)</b>
 */
public class RedisCurrentUtils
{
    @Getter
    private static RedisTemplate<String, String> template   = SpringBeanUtil.getBean("redisTemplate");
    private static long                          expireTime = SpringBeanUtil.getBean(RedisProperties.class).getExptime();

    private static Long getExpireTime()
    {
        return expireTime;
    }

    public static void setExpireTime(Long expireTime)
    {
        RedisCurrentUtils.expireTime = expireTime;
    }

    private RedisCurrentUtils() { }

    public static void batchDel(final Set<String> keys)
    {
        if (CollectionUtils.isEmpty(keys))
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis key is null");
        template.executePipelined((RedisCallback<Void>) conn -> {
            conn.openPipeline();
            keys.stream().filter(StringUtils::isNotBlank).forEach(key -> conn.del(serializerKey(key)));
            return null;
        });
    }

    public static boolean exits(final String key)
    {
        vidate(key);
        Boolean flag = template.execute(
                (RedisCallback<Boolean>) conn -> ((RedisKeyCommands) conn.getNativeConnection()).exists(serializerKey(key)));
        return flag != null && flag;
    }

    public static Boolean setNX(final String key, final String value, final Long expTime)
    {
        vidate(key, value);

        return template.execute((RedisCallback<Boolean>) conn -> getCommands(conn).set(serializerKey(key), serializerKey(value),
                Expiration.from(expTime, TimeUnit.SECONDS), RedisStringCommands.SetOption.ifAbsent()));
    }

    private static void vidate(final Object value)
    {
        vidate("1", value);
    }

    private static void vidate(final String key)
    {
        vidate(key, true);
    }

    private static void vidate(final String key, final Object value)
    {
        vidate(key, value, 1L);
    }

    private static void vidate(final String key, final Long expTime)
    {
        vidate(key, true, expTime);
    }

    private static void vidate(final String key, final Object value, final Long expTime)
    {
        if (StringUtils.isBlank(key))
        {
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis key is null");
        }
        if (Objects.isNull(value))
        {
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis value is null");
        }
        if (expTime == null || expTime <= 0)
        {
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis expTime is error");
        }
    }

    private static RedisStringCommands getCommands(final RedisConnection connection)
    {
        return (RedisStringCommands) connection.getNativeConnection();
    }

    private static byte[] serializerKey(String key)
    {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    private static String deserializeKey(byte[] bytes)
    {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
