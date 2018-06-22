package xyz.launcel.json;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import xyz.launcel.exception.SystemException;
import xyz.launcel.json.builder.PrimyGsonBuilder;

public class Json
{

    private Json() {}

    private static Gson gson;

    static { gson = new PrimyGsonBuilder().getGsonBuilder().create(); }

    private static Gson me()                   { return gson; }

    public static String toJson(Object object) { return gson.toJson(object); }

    public static <T> T toObject(String jsonObject, Class<T> clazz)
    {
        try
        {
            return gson.fromJson(jsonObject, clazz);
        }
        catch (JsonParseException x)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "Json转换异常");
        }
    }
}