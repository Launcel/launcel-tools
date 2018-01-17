package xyz.launcel.ensure;

import org.apache.commons.collections.CollectionUtils;
import xyz.launcel.exception.ExceptionFactory;

import java.util.Collection;

public class CollectAssert {

    private Collection<?> collection;

    protected CollectAssert(Collection<?> collection) {
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
