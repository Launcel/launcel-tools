package xyz.launcel.common.ensure;

import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.CollectionUtils;

import java.util.Collection;

public class CollectAssert
{

    private final Collection<?> collection;

    CollectAssert(Collection<?> collection)
    {
        this.collection = collection;
    }

    public void isEmpty(String message)
    {
        if (CollectionUtils.isEmpty(collection))
        {
            ExceptionFactory.create(message);
        }
    }

    public void notEmpty(String message)
    {
        if (CollectionUtils.isNotEmpty(collection))
        {
            ExceptionFactory.create(message);
        }
    }
}
