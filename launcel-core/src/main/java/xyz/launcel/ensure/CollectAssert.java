package xyz.launcel.ensure;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.CollectionUtils;

import java.util.Collection;

public class CollectAssert {

    private Collection<?> collection;

    CollectAssert(Collection<?> collection) {
        this.collection = collection;
    }


    public void isEmpty(String message) {
        if (CollectionUtils.isEmpty(collection))
            ExceptionFactory.create(message);
    }


    public void notEmpty(String message) {
        if (CollectionUtils.isNotEmpty(collection))
            ExceptionFactory.create(message);
    }

}
