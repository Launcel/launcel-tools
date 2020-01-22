package xyz.launcel.ensure;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.utils.CollectionUtils;

import java.util.Map;

/**
 * Created by launcel on 2018/9/2.
 */
public class MapAssert
{
    private Map<?, ?> map;

    MapAssert(Map<?, ?> map)
    {
        this.map = map;
    }

    public void isEmpty(String message)
    {
        if (CollectionUtils.isEmpty(map))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notEmpty(String message)
    {
        if (CollectionUtils.isNotEmpty(map))
        {
            ExceptionFactory.create(message);
        }
    }

    public BooleanAssert contains(Object key)
    {
        return new BooleanAssert(map.containsKey(key));
    }
}
