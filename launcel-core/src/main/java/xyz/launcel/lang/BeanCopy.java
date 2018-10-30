package xyz.launcel.lang;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.SystemException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class BeanCopy
{

    private BeanCopy() { }

    private static <T> T mapProperties(@NonNull Object source, T target, String... ignoreProperties)
    {
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    private static <T> T mapProperties(Object source, T target)
    {
        return mapProperties(source, target, (String[]) null);
    }

    public static void copy(Object source, Object target)                             { copy(source, target, (String[]) null); }

    public static void copy(Object source, Object target, String... ignoreProperties) { mapProperties(source, target, ignoreProperties); }

    public static <T> T copy(Object source, Class<T> targetClass)                     { return copy(source, targetClass, (String[]) null); }

    public static <T> T copy(Object source, Class<T> targetClass, String... ignoreProperties)
    {
        T target;
        try
        {
            target = targetClass.getDeclaredConstructor().newInstance();
            mapProperties(source, target, ignoreProperties);
            return target;
        }
        catch (ReflectiveOperationException e)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", targetClass.getSimpleName() + "实例化异常");
        }
    }

    public static <T> List<T> copy(Collection<?> source, Class<T> targetClass)
    {
        return copy(source, targetClass, (String[]) null);
    }

    public static <T> List<T> copy(Collection<?> source, Class<T> targetClass, String... ignoreProperties)
    {
        if (CollectionUtils.isEmpty(source))
        { ExceptionFactory.create("_DEFINE_ERROR_CODE_012", "集合中没有数据"); }
        try
        {
            var targetList = new ArrayList<T>();
            var target     = targetClass.getDeclaredConstructor().newInstance();
            source.forEach(s -> targetList.add(mapProperties(s, target)));
            return targetList;
        }
        catch (ReflectiveOperationException e)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", targetClass.getSimpleName() + "实例化异常");
        }
    }
}
