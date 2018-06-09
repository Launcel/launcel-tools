package xyz.launcel.lang;

import xyz.launcel.exception.ProfessionException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
        if (object instanceof Collection)
        {
            return ((Collection) object).isEmpty();
        }
        else if (object instanceof Map)
        {
            return ((Map) object).isEmpty();
        }
        else if (object instanceof Object[])
        {
            return ((Object[]) object).length == 0;
        }
        else if (object instanceof Iterator)
        {
            return !((Iterator) object).hasNext();
        }
        else if (object instanceof Enumeration)
        {
            return !((Enumeration) object).hasMoreElements();
        }
        else if (object == null)
        {
            throw new IllegalArgumentException("Unsupported object type: null");
        }
        else
        {
            try
            {
                return Array.getLength(object) == 0;
            }
            catch (IllegalArgumentException ex)
            {
                throw new ProfessionException("_DEFINE_ERROR_CODE_011", "Unsupported object type" + object.getClass().getName());
            }
        }
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
    static <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map)
    {
        try
        {
            Field tail = map.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            //noinspection unchecked
            return (Map.Entry<K, V>) tail.get(map);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
