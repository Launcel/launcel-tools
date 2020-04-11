package xyz.launcel.redis.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyz.launcel.redis.support.serializer.GsonRedisSerializer;
import xyz.launcel.redis.support.serializer.KeyRedisSerializer;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class RedisTemplates extends RedisTemplate<String, Object> implements RedisOperation
{

    private boolean enableTransactionSupport = false;
    private boolean exposeConnection         = false;
    private boolean initialized              = false;
    private boolean enableDefaultSerializer  = true;

    private RedisSerializer<?> defaultSerializer;

    private RedisSerializer keySerializer;
    private RedisSerializer valueSerializer;
    private RedisSerializer hashKeySerializer;
    private RedisSerializer hashValueSerializer;

    private final KeyRedisSerializer    defaultKeySerializer     = new KeyRedisSerializer();
    private final StringRedisSerializer defaultHashKeySerializer = new StringRedisSerializer();
    private final RedisSerializer       defaultValueSerializer   = new JdkSerializationRedisSerializer();

    public RedisTemplates()
    {
        initialized = true;

        this.setKeySerializer(defaultKeySerializer);
        this.setHashKeySerializer(defaultHashKeySerializer);

        this.setValueSerializer(defaultValueSerializer);
        this.setHashValueSerializer(defaultValueSerializer);
        this.setDefaultSerializer(new GsonRedisSerializer<>(Object.class));
    }

    public void setDefaultSerializer(RedisSerializer<?> defaultSerializer)
    {
        this.defaultSerializer = Objects.nonNull(defaultSerializer) ? defaultSerializer : new GsonRedisSerializer<>(Object.class);
        super.setDefaultSerializer(this.defaultSerializer);
    }

    public void setKeySerializer(RedisSerializer keySerializer)
    {
        this.keySerializer = Objects.nonNull(keySerializer) ? keySerializer : defaultKeySerializer;
        super.setKeySerializer(this.keySerializer);
    }

    public void setHashKeySerializer(RedisSerializer hashKeySerializer)
    {
        this.hashKeySerializer = Objects.nonNull(hashKeySerializer) ? hashKeySerializer : defaultHashKeySerializer;
        super.setHashKeySerializer(this.hashKeySerializer);
    }

    public void setValueSerializer(RedisSerializer valueSerializer)
    {
        this.valueSerializer = Objects.nonNull(valueSerializer) ? valueSerializer : defaultValueSerializer;
        super.setValueSerializer(this.valueSerializer);
    }


    public void setHashValueSerializer(RedisSerializer hashValueSerializer)
    {
        this.hashValueSerializer = Objects.nonNull(hashValueSerializer) ? hashValueSerializer : defaultValueSerializer;
        super.setHashValueSerializer(this.hashValueSerializer);
    }
}
