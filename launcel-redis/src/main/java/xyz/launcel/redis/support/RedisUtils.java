package xyz.launcel.redis.support;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.exception.SystemException;
import xyz.launcel.redis.core.RedisOperation;
import xyz.launcel.redis.properties.RedisProperties;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.Json;
import xyz.launcel.utils.StringUtils;

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
    private static RedisOperation template   = SpringBeanUtil.getBean(RedisOperation.class);
    private static long           expireTime = SpringBeanUtil.getBean(RedisProperties.class).getExptime();


    public static String getNewKey(String key)
    {
        return RedisProperties.getPrefixKey().concat(key);
    }

    public static void batchDel(final Set<String> keys)
    {
        if (CollectionUtils.isEmpty(keys))
        {
            throw new SystemException("_REDIS__ERROR_CODE_011", "redis key is null");
        }
        RedisCallback<Void> callback = conn -> {
            conn.openPipeline();
            keys.stream().filter(StringUtils::isNotBlank).forEach(key -> conn.del(StringUtils.serializer(getNewKey(key))));
            conn.closePipeline();
            return null;
        };
        template.execute(callback);
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
            template.executePipelined(callback);
        }
        return act.get();
    }

    public static boolean exits(final String key)
    {
        vidate(key);
        var flat = template.hasKey(getNewKey(key));
        return flat != null && flat;
    }

    public static boolean setNxNANO(final String key, final String value, final Long expTime)
    {
        vidate(key, value, expTime);
        RedisCallback<Boolean> callback = conn -> conn.set(StringUtils.serializer(getNewKey(key)), StringUtils.serializer(value),
                Expiration.from(expTime, TimeUnit.NANOSECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
        var flat = template.execute(callback);
        return flat != null && flat;
    }

    public static boolean setNX(final String key, final String value, final Long expTime)
    {
        return setNxNANO(key, value, expTime * 1000000);
    }

    public static boolean del(final String key)
    {
        var flat = template.delete(getNewKey(key));
        return flat != null && flat;
    }

    public static boolean unclock(final String key)
    {

        String                 script   = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisCallback<Boolean> callback = connection -> connection.scriptingCommands().eval(script.getBytes(), ReturnType.BOOLEAN, 1, "1".getBytes());
        var                    flat     = template.execute(callback);
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
            throw new SystemException("0301");
        }
        if (Objects.isNull(value))
        {
            throw new SystemException("0302");
        }
        if (expTime == null || expTime <= 0)
        {
            throw new SystemException("0303");
        }
    }

}
