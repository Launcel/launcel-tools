package xyz.launcel.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class MathSet
{
    protected Integer set(Integer integer)
    {
        return Objects.nonNull(integer) ? integer : 0;
    }

    private BigDecimal set(BigDecimal bigDecimal)
    {
        return Objects.nonNull(bigDecimal) ? bigDecimal : BigDecimal.ZERO;
    }

    private BigInteger set(BigInteger bigInteger)
    {
        return Objects.nonNull(bigInteger) ? bigInteger : BigInteger.ZERO;
    }

    protected Long set(Long l)
    {
        return Objects.nonNull(l) ? l : 0L;
    }
}
