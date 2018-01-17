package xyz.launcel.support.serializer;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import xyz.launcel.lang.PrimyGsonBuilder;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class GsonRedisSerializer<T> implements RedisSerializer<T> {

    private GsonBuilder gsonBuilder = new PrimyGsonBuilder().getGsonBuilder();

    private Class<T> type;

    public GsonRedisSerializer(Class<T> type) {
        this.type = type;
    }

//    public GsonBuilder getGsonBuilder() {
//        return gsonBuilder;
//    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null)
            return new byte[0];
        return gsonBuilder.create().toJson(t).getBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0)
            return null;
        String str;
        try {
            str = new String(bytes, "UTF-8");
            JsonReader reader = new JsonReader(new StringReader(str));
            reader.setLenient(true);
            return gsonBuilder.create().fromJson(str, type);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


}
