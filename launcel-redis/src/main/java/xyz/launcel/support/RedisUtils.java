package xyz.launcel.support;

import lombok.Getter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import xyz.launcel.bean.context.SpringBeanUtil;
import xyz.launcel.exception.SystemException;
import xyz.launcel.json.Json;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.properties.RedisProperties;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>普通情况下，建议用RedisConnection(RedisTemplate是基于RedisConnection) 操作，pipeline性能高，</b><br/>
 * 控制并发数据时，用Commands,见setNX方法<b>(但只能处理String类型)</b>
 */
public final class RedisUtils
{
    @Getter
    private static RedisTemplate<String, String> template   = SpringBeanUtil.getBean("redisTemplate");
    @Getter
    private static long                          expireTime = SpringBeanUtil.getBean(RedisProperties.class).getExptime();

    private RedisUtils() { }

    public static void batchDel(final Set<String> keys)
    {
        if (CollectionUtils.isEmpty(keys))
        {
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis key is null");
        }
        RedisCallback<Void> callback = conn -> {
            conn.openPipeline();
            keys.stream().filter(StringUtils::isNotBlank).forEach(key -> conn.del(StringUtils.serializer(key)));
            conn.closePipeline();
            return null;
        };
        template.execute(callback);
    }

    public static int batchAdd(final Map<String, Object> map)
    {
        AtomicInteger act = new AtomicInteger(0);
        if (CollectionUtils.isNotEmpty(map))
        {
            RedisCallback<Void> callback = conn -> {
                conn.openPipeline();
                int num = 0;
                for (Map.Entry<String, Object> entry : map.entrySet())
                {
                    if (StringUtils.isNotBlank(entry.getKey()))
                    {
                        Boolean result = conn.setEx(StringUtils.serializer(entry.getKey()), expireTime,
                                StringUtils.serializer(Json.toString(entry.getValue())));
                        if (result != null && result)
                        {
                            num++;
                        }
                    }
                }
                conn.closePipeline();
                act.set(num);
                return null;
            };
            template.executePipelined(callback);
        }
        return act.get();
    }

    public static boolean exits(final String key)
    {
        vidate(key);
        Boolean flat = template.hasKey(key);
        return flat != null && flat;
    }

    public static boolean setNX(final String key, final String value, final Long expTime)
    {
        vidate(key, value, expTime);
        RedisCallback<Boolean> callback = conn -> conn.set(StringUtils.serializer(key), StringUtils.serializer(value),
                Expiration.from(expTime, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
        Boolean flat = template.execute(callback);
        return flat != null && flat;
    }

    private static void vidate(final String key)
    {
        vidate(key, true);
    }

    private static void vidate(final String key, final Object value)
    {
        vidate(key, value, 1L);
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
}
