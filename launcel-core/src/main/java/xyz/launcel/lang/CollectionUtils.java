package xyz.launcel.lang;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public interface CollectionUtils
{

    static boolean isNotEmpty(Collection coll)
    {
        return !isEmpty(coll);
    }

    static boolean isNotEmpty(Map coll)
    {
        return !isEmpty(coll);
    }

    static boolean isEmpty(Map coll)
    {
        return (coll == null || coll.isEmpty());
    }

    static boolean isEmpty(Collection coll)
    {
        return (coll == null || coll.isEmpty());
    }

    static boolean sizeIsEmpty(Object object)
    {
        if (object == null || object.getClass() == null)
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "Unsupported object type: null");
        if (object instanceof Collection)
            return ((Collection) object).isEmpty();
        if (object instanceof Map)
            return ((Map) object).isEmpty();
        if (object instanceof Object[])
            return ((Object[]) object).length == 0;
        if (object instanceof Iterator)
            return !((Iterator) object).hasNext();
        if (object instanceof Enumeration)
            return !((Enumeration) object).hasMoreElements();

        try
        {
            return Array.getLength(object) == 0;
        }
        catch (IllegalArgumentException ex)
        {
            ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "Unsupported object type" + object.getClass().getName());
        }
        return false;
    }

    /**
     * 获得第一个头节点数据
     *
     * @param map LinkedHashMap
     * @param <K> key
     * @param <V> value
     *
     * @return Map.Entry&lt;K, V&gt;
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
     *
     * @return Map.Entry&lt;K, V&gt;
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
            RootLogger.error("", e.getCause());
            return null;
        }
    }

}
