package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.StringUtils;

import java.util.Objects;

public class BooleanAssert
{
    private final Boolean flat;

    BooleanAssert(Boolean fa)
    {
        flat = fa;
    }

    public void isTrue(String message)
    {
        if (Objects.nonNull(flat) && flat)
        {
            ExceptionFactory.create(message);
        }
    }

    public void isFalse(String message)
    {
        if (Objects.nonNull(flat) && !flat)
        {
            ExceptionFactory.create(message);
        }
    }
}
