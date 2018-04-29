package xyz.launcel.json;

//import com.alibaba.fastjson.JSON;
//
//public interface Json {
//
//
//
//    static String toJson(Object object) {
//        return JSON.toJSONString(object);
//    }
//
//    static <T> T toObject(String jsonObject, Class<T> clazz) {
//        return JSON.parseObject(jsonObject, clazz);
//    }
//
//}

import com.google.gson.Gson;

public class Json {
    
    private Json() {
    }
    
    private static Gson gson;
    
    static {
        gson = new PrimyGsonBuilder().getGsonBuilder().create();
    }
    
    public static Gson me() {
        return gson;
    }
    
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
    
    public static <T> T parseObject(String jsonObject, Class<T> clazz) {
        return gson.fromJson(jsonObject, clazz);
    }
}