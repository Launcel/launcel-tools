package xyz.launcel.redis.support;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;
import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.common.utils.Json;
import xyz.launcel.common.utils.StringUtils;
import xyz.launcel.redis.core.RedisOperation;
import xyz.launcel.redis.properties.RedisProperties;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>普通情况下，建议用RedisConnection(RedisTemplate是基于RedisConnection) 操作，pipeline性能高，</b><br/>
 * 控制并发数据时，用Commands,见setNX方法<b>(但只能处理String类型)</b>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisUtils
{
    private static RedisOperation operation;
    private static long           expireTime = RedisProperties.getExpTime();


    public static String getNewKey(String key)
    {
        return RedisProperties.getPrefixKey().concat(key);
    }

    public static void batchDel(final Set<String> keys)
    {
        if (CollectionUtils.isEmpty(keys))
        {
            ExceptionFactory.create("_REDIS__ERROR_CODE_011", "redis key is null");
        }
        RedisCallback<Void> callback = conn -> {
            conn.openPipeline();
            keys.stream().filter(StringUtils::isNotBlank).forEach(key -> conn.del(StringUtils.serializer(getNewKey(key))));
            conn.closePipeline();
            return null;
        };
        operation.execute(callback);
    }

    public static int batchAdd(final Map<String, Object> map)
    {
        var act = new AtomicInteger(0);
        if (CollectionUtils.isNotEmpty(map))
        {
            RedisCallback<Void> callback = conn -> {
                conn.openPipeline();
                var num = 0;
                for (Map.Entry<String, Object> entry : map.entrySet())
                {
                    if (StringUtils.isNotBlank(entry.getKey()))
                    {
                        var result = conn.setEx(StringUtils.serializer(getNewKey(entry.getKey())), expireTime,
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
            operation.executePipelined(callback);
        }
        return act.get();
    }

    public static boolean exits(final String key)
    {
        vidate(key);
        var flat = operation.hasKey(getNewKey(key));
        return flat != null && flat;
    }

    public static boolean setNxNANO(final String key, final String value, final Long expTime)
    {
        vidate(key, value, expTime);
        RedisCallback<Boolean> callback = conn -> conn.set(StringUtils.serializer(getNewKey(key)), StringUtils.serializer(value),
                Expiration.from(expTime, TimeUnit.NANOSECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
        var flat = operation.execute(callback);
        return flat != null && flat;
    }

    public static boolean setNX(final String key, final String value, final Long expTime)
    {
        return setNxNANO(key, value, expTime * 1000000);
    }

    public static boolean del(final String key)
    {
        var flat = operation.delete(getNewKey(key));
        return flat != null && flat;
    }

    public static boolean unclock(final String key)
    {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisCallback<Boolean> callback = connection -> connection.scriptingCommands()
                .eval(script.getBytes(), ReturnType.BOOLEAN, 1, getNewKey(key).getBytes());
        var flat = operation.execute(callback);
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
            ExceptionFactory.create("0301");
        }
        if (Objects.isNull(value))
        {
            ExceptionFactory.create("0302");
        }
        if (expTime == null || expTime <= 0)
        {
            ExceptionFactory.create("0303");
        }
    }

    public static void afterPropertiesSet(RedisOperation operation)
    {
        RedisUtils.operation = operation;
    }
}
