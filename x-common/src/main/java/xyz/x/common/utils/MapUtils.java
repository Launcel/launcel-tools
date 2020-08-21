package xyz.x.common.utils;

import lombok.var;
import xyz.x.common.exception.SystemException;

import java.util.LinkedHashMap;
import java.util.Map;

public interface MapUtils
{
    /**
     * 获得第一个头节点数据
     *
     * @param map LinkedHashMap
     * @param <K> key
     * @param <V> value
     * @return {@code Map.Entry<K, V>}
     */
    static <K, V> Map.Entry<K, V> getHead(LinkedHashMap<K, V> map)
    {
        return map.entrySet().iterator().next();
    }

    /**
     * 获得最后一个节点数据
     *
     * @param map LinkedHashMap
     * @param <K> key
     * @param <V> value
     * @return {@code Map.Entry<K, V>}
     */
    @SuppressWarnings({"unchecked"})
    static <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map)
    {
        try
        {
            var tail = map.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            return (Map.Entry<K, V>) tail.get(map);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
        throw new SystemException("0025");
    }
}