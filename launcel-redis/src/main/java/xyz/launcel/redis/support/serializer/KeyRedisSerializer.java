package xyz.launcel.redis.support.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import xyz.launcel.log.Log;
import xyz.launcel.redis.properties.RedisProperties;
import xyz.launcel.common.utils.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KeyRedisSerializer implements RedisSerializer<String>
{

    private final Charset charset;

    public KeyRedisSerializer()
    {
        this(StandardCharsets.UTF_8);
    }

    public KeyRedisSerializer(Charset charset)
    {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes)
    {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(@Nullable String string)
    {
        if (StringUtils.isBlank(string))
        {
            return null;
        }
        Log.info(RedisProperties.getPrefixKey().concat(string));
        return RedisProperties.getPrefixKey().concat(string).getBytes(charset);
    }

}