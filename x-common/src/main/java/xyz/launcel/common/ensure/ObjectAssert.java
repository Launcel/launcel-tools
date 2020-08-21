package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;

import java.util.Objects;

public class ObjectAssert
{

    private Object flat;

    ObjectAssert(Object flat)
    {
        this.flat = flat;
    }

    public void isNull(String message)
    {
        if (Objects.isNull(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notNull(String message)
    {
        if (Objects.nonNull(flat))
        {
            ExceptionFactory.create(message);
        }
    }

    public BooleanAssert eq(Object o)
    {
        return new BooleanAssert(Objects.equals(o, flat));
    }
}
