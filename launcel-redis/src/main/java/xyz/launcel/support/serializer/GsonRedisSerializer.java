package xyz.launcel.support.serializer;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import xyz.launcel.utils.json.builder.DefaultGsonBuilder;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@AllArgsConstructor
public class GsonRedisSerializer<T> implements RedisSerializer<T>
{
    private Gson gson = DefaultGsonBuilder.create();

    private Class<T> type;

    public GsonRedisSerializer(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return gson.toJson(t).getBytes();
    }

    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        var str = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(str, type);
    }
}
