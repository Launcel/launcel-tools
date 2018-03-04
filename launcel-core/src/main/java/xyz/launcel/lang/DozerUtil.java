/**
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.10.14
 * Version: 1.0
 */

package xyz.launcel.lang;

import org.dozer.Mapper;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.ApplicationContextHook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface DozerUtil {

    class DozerMapperInstance {
        private static Mapper dozer = ApplicationContextHook.getBean("dozer");
    }


    static <T> T map(Object source, Class<T> destinationClass) {
        return DozerMapperInstance.dozer.map(source, destinationClass);
    }


    /**
     * transform list to anthor list
     *
     * @param var1 param list
     * @param var2 result list of the class instance
     * @param <T>
     * @return
     */
    static <T> List<T> map(Collection<?> var1, Class<T> var2) {
        if (CollectionUtils.isEmpty(var1))
            ExceptionFactory.create("_DEFINE_ERROR_CODE_009", "集合中没有数据");
        List<T> var2List = new ArrayList<>();
        for (Object aVar1 : var1) {
            T var = DozerMapperInstance.dozer.map(aVar1, var2);
            var2List.add(var);
        }
        return var2List;
    }
}
