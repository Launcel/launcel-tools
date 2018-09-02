/**
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.11.17
 * Version: 1.0
 */

package xyz.launcel.ensure;

import java.util.Collection;
import java.util.Map;

public interface Me
{

    static BooleanAssert that(Boolean flat)
    {
        return new BooleanAssert(flat);
    }

    static ObjectAssert that(Object o)
    {
        return new ObjectAssert(o);
    }

    static StringAssert that(String str)
    {
        return new StringAssert(str);
    }

    static CollectAssert that(Collection<?> collection)
    {
        return new CollectAssert(collection);
    }

    static MapAssert that(Map<?, ?> map)
    {
        return new MapAssert(map);
    }


}

