package xyz.launcel.utils.json.builder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonMapDeserializer implements JsonDeserializer<Object>
{
    @Override
    public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    {

        JsonObject                          jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet   = jsonObject.entrySet();
        Map<String, Object>                 treeMap    = new HashMap<>();
        entrySet.forEach(k -> treeMap.put(k.getKey(), k.getValue()));
        return treeMap;
    }
}

@NoArgsConstructor(access = AccessLevel.PUBLIC)
class MapTypeAdapter extends TypeAdapter<Object>
{

    @Override
    public void write(JsonWriter out, Object value) { }

    @Override
    public Object read(JsonReader in) throws IOException
    {
        var token = in.peek();
        switch (token)
        {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext())
                {
                    list.add(read(in));
                }
                in.endArray();
                return list;
            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext())
                {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;
            case STRING:
                return in.nextString();
            case NUMBER:
                /**
                 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                 */
                double dbNum = in.nextDouble();
                // 数字超过long的最大值，返回浮点类型
                if (dbNum > Long.MAX_VALUE)
                {
                    return dbNum;
                }
                // 判断数字是否为整数值
                long lngNum = (long) dbNum;
                if (dbNum == lngNum)
                {
                    return lngNum;
                }
                return dbNum;
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }
}
