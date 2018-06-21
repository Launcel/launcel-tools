package xyz.launcel.lang;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.ProfessionException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public interface CollectionUtils {
    
    static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }
    
    static boolean isNotEmpty(Map coll) {
        return !isEmpty(coll);
    }
    
    static boolean isEmpty(Map coll) {
        return (coll == null || coll.isEmpty());
    }
    
    static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }
    
    static boolean sizeIsEmpty(Object object) {
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        } else if (object instanceof Iterator) {
            return !((Iterator) object).hasNext();
        } else if (object instanceof Enumeration) {
            return !((Enumeration) object).hasMoreElements();
        } else if (object == null) {
            throw new IllegalArgumentException("Unsupported object type: null");
        } else {
            try {
                return Array.getLength(object) == 0;
            } catch (IllegalArgumentException ex) {
                ExceptionFactory.create("_DEFINE_ERROR_CODE_011", "Unsupported object type" + object.getClass().getName());
            }
        }
        return false;
    }
    
}
