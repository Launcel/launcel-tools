package xyz.launcel.ensure;

import java.util.Collection;
import java.util.Map;

public interface Me
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


}

