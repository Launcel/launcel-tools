package xyz.launcel.lang;

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
