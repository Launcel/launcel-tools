package xyz.launcel.ensure;

import xyz.launcel.exception.ExceptionFactory;

public class BooleanAssert
{
    private Boolean flat;

    BooleanAssert(Boolean fa)
    {
        flat = fa;
    }

    public void isTrue(String message)
    {
        if (flat) { ExceptionFactory.create(message); }
    }

    public void isFalse(String message)
    {
        if (!flat) { ExceptionFactory.create(message); }
    }

}
