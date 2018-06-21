package xyz.launcel.lang;

import com.google.gson.Gson;
import xyz.launcel.json.support.PrimyGsonBuilder;

public class Json {

    private Json() {
    }

    private static Gson gson;

    static {
        gson = new PrimyGsonBuilder().getGsonBuilder().create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T toObject(String jsonObject, Class<T> clazz) {
        return gson.fromJson(jsonObject, clazz);
    }
}