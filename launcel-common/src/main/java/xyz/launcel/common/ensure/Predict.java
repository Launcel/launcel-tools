package xyz.launcel.common.ensure;

import java.util.Collection;
import java.util.Map;

public interface Predict
{
    static BooleanAssert builder(Boolean flat)
    {
        return new BooleanAssert(flat);
    }

    static ObjectAssert builder(Object o)
    {
        return new ObjectAssert(o);
    }

    static StringAssert builder(String str)
    {
        return new StringAssert(str);
    }

    static CollectAssert builder(Collection<?> collection)
    {
        return new CollectAssert(collection);
    }

    static MapAssert builder(Map<?, ?> map)
    {
        return new MapAssert(map);
    }

    static <T> ComparableAssert<T> builder(Comparable<T> t)
    {
        return new ComparableAssert<>(t);
    }
}

