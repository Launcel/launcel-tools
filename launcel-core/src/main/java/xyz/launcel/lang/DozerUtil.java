/**
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.10.14
 * Version: 1.0
 */

package xyz.launcel.lang;

import org.dozer.Mapper;
import xyz.launcel.ensure.Me;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DozerUtil {
    private Mapper dozer;

    private DozerUtil(Mapper dozer) {
        this.dozer = dozer;
    }

    public static DozerUtil me(Mapper dozer) {
        return new DozerUtil(dozer);
    }

    /**
     * transform list to anthor list
     *
     * @param var1 param list
     * @param var2 result list of the class instance
     * @param <T>
     * @return
     */
    public <T> List<T> map(Collection<?> var1, Class<T> var2) {
        Me.that(var1).isEmpty("_DEFINE_ERROR_CODE_009");
        List<T> var2List = new ArrayList<>();
        for (Iterator<?> it = var1.iterator(); it.hasNext(); ) {
            T var = dozer.map(it.next(), var2);
            var2List.add(var);
        }
        return var2List;
    }
}
