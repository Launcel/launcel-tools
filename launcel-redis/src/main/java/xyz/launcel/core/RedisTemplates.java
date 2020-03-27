package xyz.launcel.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyz.launcel.support.serializer.GsonRedisSerializer;

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

    private       RedisSerializer<?>    defaultSerializer;
    private       RedisSerializer       keySerializer;
    private       RedisSerializer       valueSerializer;
    private       RedisSerializer       hashKeySerializer;
    private       RedisSerializer       hashValueSerializer;
    private final StringRedisSerializer defaultKeySerializer   = new StringRedisSerializer();
    private final RedisSerializer       defaultValueSerializer = new JdkSerializationRedisSerializer();

    public RedisTemplates()
    {
        initialized = true;

        super.setKeySerializer(defaultKeySerializer);
        super.setHashKeySerializer(defaultKeySerializer);

        super.setValueSerializer(defaultValueSerializer);
        super.setHashValueSerializer(defaultValueSerializer);
        super.setDefaultSerializer(new GsonRedisSerializer<>(Object.class));
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
        this.hashKeySerializer = Objects.nonNull(hashKeySerializer) ? hashKeySerializer : defaultKeySerializer;
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
