package xyz.launcel.json;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import xyz.launcel.exception.SystemException;
import xyz.launcel.json.builder.DefaultGsonBuilder;
import xyz.launcel.json.builder.GenericsParameterizedType;

import java.util.List;

public class Json
{

    private Json() {}

    private static Gson gson = DefaultGsonBuilder.getGsonBuilder().create();

    public static Gson me()
    { return gson; }

    public static String toString(Object object) { return gson.toJson(object); }

    public static <T> T parse(final String jsonObject, final Class<T> clazz)
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

    public static <T> List<T> parseArray(final String json, final Class<T> t)
    {
        try
        {
            return gson.fromJson(json, new GenericsParameterizedType(t));
        }
        catch (JsonParseException x)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "Json转换异常");
        }

    }

    //    public static <T> List<T> toObjectList(final String json, final Class<T> t)
    //    {
    //        try
    //        {
    //            List<T>   lst   = new ArrayList<>();
    //            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
    //            for (final JsonElement element : array)
    //            {
    //                lst.add(gson.fromJson(element, t));
    //            }
    //            return lst;
    //        }
    //        catch (Exception e)
    //        {
    //            return null;
    //        }
    //}
}

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONException;
//import xyz.launcel.exception.SystemException;
//
//import java.util.List;
//
//public class Json
//{
//    public static String toJson(Object object) { return JSON.toJSONString(object); }
//
//    public static <T> T toObject(final String jsonObject, final Class<T> clazz)
//    {
//        try
//        {
//            return JSON.parseObject(jsonObject, clazz);
//        }
//        catch (JSONException x)
//        {
//            throw new SystemException("_DEFINE_ERROR_CODE_011", "Json转换异常");
//        }
//    }
//
//    public static <T> List<T> toObjectList(final String json, final Class<T> t)
//    {
//        try
//        {
//            return JSON.parseArray(json, t);
//        }
//        catch (JSONException e)
//        {
//            throw new SystemException("_DEFINE_ERROR_CODE_011", "Json转换异常");
//        }
//    }
//}