package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;

public class BooleanAssert
{
    private final Boolean flat;

    BooleanAssert(Boolean fa)
    {
        flat = fa;
    }

    public void isTrue(String message)
    {
        if (flat)
        {
            ExceptionFactory.create(message);
        }
    }

    public void isFalse(String message)
    {
        if (!flat)
        {
            ExceptionFactory.create(message);
        }
    }
}
