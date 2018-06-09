/*
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.11.17
 * Version: 1.0
 */

package xyz.launcel.ensure;

import java.util.Collection;

public final class Me
{

    private Me() { }

    public static BooleanAssert that(Boolean flat)
    {
        return new BooleanAssert(flat);
    }

    public static ObjectAssert that(Object o)
    {
        return new ObjectAssert(o);
    }

    public static StringAssert that(String str)
    {
        return new StringAssert(str);
    }

    public static CollectAssert that(Collection<?> collection)
    {
        return new CollectAssert(collection);
    }


}

