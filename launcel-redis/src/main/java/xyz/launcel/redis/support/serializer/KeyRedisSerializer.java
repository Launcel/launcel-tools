package xyz.launcel.redis.support.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import xyz.launcel.log.Log;
import xyz.launcel.utils.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KeyRedisSerializer implements RedisSerializer<String>
{

    private final Charset charset;

    private String prefixKey;

    public KeyRedisSerializer()
    {
        this(StandardCharsets.UTF_8);
    }

    public KeyRedisSerializer(Charset charset)
    {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    public KeyRedisSerializer(String prefixKey)
    {
        this(StandardCharsets.UTF_8);
        this.prefixKey = prefixKey;
    }

    public KeyRedisSerializer(Charset charset, String prefixKey)
    {
        this.charset = charset;
        this.prefixKey = prefixKey;
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
        if (StringUtils.isNotBlank(prefixKey))
        {
            Log.info(prefixKey.concat(string));
            return prefixKey.concat(string).getBytes(charset);
        }
        Log.info(prefixKey.concat(string));
        return string.getBytes(charset);
    }

}