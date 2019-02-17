package xyz.launcel.support.serializer;

import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import xyz.launcel.json.builder.DefaultGsonBuilder;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@AllArgsConstructor
public class GsonRedisSerializer<T> implements RedisSerializer<T>
{
    private GsonBuilder gsonBuilder = DefaultGsonBuilder.getGsonBuilder();

    private Class<T> type;

    public GsonRedisSerializer(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return gsonBuilder.create().toJson(t).getBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length == 0)
        {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return gsonBuilder.create().fromJson(str, type);
    }


}
