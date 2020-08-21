package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;

import java.util.Objects;

public class ComparableAssert<T>
{
    private final Comparable<T> comparable;

    ComparableAssert(Comparable<T> comparable)
    {
        this.comparable = comparable;
    }

    public BooleanAssert eq(T t)
    {
        return new BooleanAssert(comparable.compareTo(t) == 0);
    }

    public BooleanAssert gt(T t)
    {
        return new BooleanAssert(comparable.compareTo(t) > 0);
    }

    public BooleanAssert gtOrEq(T t)
    {
        return new BooleanAssert(comparable.compareTo(t) >= 0);
    }

    public BooleanAssert lt(T t)
    {
        return new BooleanAssert(comparable.compareTo(t) < 0);
    }

    public BooleanAssert ltOrEq(T t)
    {
        return new BooleanAssert(comparable.compareTo(t) <= 0);
    }

    public void isNull(String message)
    {
        if (Objects.isNull(comparable))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notNull(String message)
    {
        if (Objects.nonNull(comparable))
        {
            ExceptionFactory.create(message);
        }
    }


}
