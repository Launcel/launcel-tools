package xyz.launcel.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import xyz.launcel.exception.SystemException;
import xyz.launcel.utils.json.builder.DefaultGsonBuilder;
import xyz.launcel.utils.json.builder.GenericsParameterizedType;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Json
{

    private static Gson gson = DefaultGsonBuilder.create();

    public static String toString(Object object) { return gson.toJson(object); }

    public static <T> T parse(@NonNull final String jsonObject, @NonNull final Class<T> clazz)
    {
        try
        {
            return gson.fromJson(jsonObject, clazz);
        }
        catch (JsonParseException x)
        {
            throw new SystemException("0026");
        }
    }

    public static <T> List<T> parseArray(@NonNull final String json, @NonNull final Class<T> t)
    {
        try
        {
            return gson.fromJson(json, new GenericsParameterizedType(t));
        }
        catch (JsonParseException x)
        {
            throw new SystemException("0026");
        }
    }
}
