/**
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.11.18
 * Version: 1.0
 */

package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.StringUtils;

public class StringAssert
{
    private final String flat;

    StringAssert(String f)
    {
        flat = f;
    }

    public void isEmpty(String message)
    {
        if (StringUtils.isEmpty(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public void isBlank(String message)
    {
        if (StringUtils.isBlank(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notEmpty(String message)
    {
        if (!StringUtils.isNotEmpty(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notBlank(String message)
    {
        if (StringUtils.isNotBlank(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public BooleanAssert equal(String str)
    {
        return new BooleanAssert(org.apache.commons.lang3.StringUtils.equals(flat, str));
    }

    public BooleanAssert equalIgnoreCase(String str)
    {
        return new BooleanAssert(org.apache.commons.lang3.StringUtils.containsIgnoreCase(flat, str));
    }

}
