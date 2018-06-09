package xyz.launcel.lang;

import org.springframework.beans.BeanUtils;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.ProfessionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanCopy
{

    private BeanCopy()
    {
    }

    private static <T> T mapProperties(Object source, T target, String... ignoreProperties)
    {
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    private static <T> T mapProperties(Object source, T target)                      { return mapProperties(source, target, (String[]) null); }

    public static void map(Object source, Object target)                             { map(source, target, (String[]) null); }

    public static void map(Object source, Object target, String... ignoreProperties) { mapProperties(source, target, ignoreProperties); }

    public static <T> T map(Object source, Class<T> targetClass)                     { return map(source, targetClass, (String[]) null); }

    public static <T> T map(Object source, Class<T> targetClass, String... ignoreProperties)
    {
        T target;
        try
        {
            target = targetClass.newInstance();
            mapProperties(source, target, ignoreProperties);
            return target;
        }
        catch (ReflectiveOperationException e)
        {
            throw new ProfessionException("_DEFINE_ERROR_CODE_010", targetClass.getSimpleName() + "实例化异常");
        }
    }

    public static <T> List<T> map(Collection<?> source, Class<T> targetClass)
    {
        return map(source, targetClass, (String[]) null);
    }

    public static <T> List<T> map(Collection<?> source, Class<T> targetClass, String... ignoreProperties)
    {
        if (CollectionUtils.isEmpty(source)) { ExceptionFactory.create("_DEFINE_ERROR_CODE_009", "集合中没有数据"); }
        try
        {
            List<T> targetList = new ArrayList<>();
            T       target     = targetClass.newInstance();
            source.forEach(s -> targetList.add(mapProperties(s, target)));
            return targetList;
        }
        catch (ReflectiveOperationException e)
        {
            throw new ProfessionException("_DEFINE_ERROR_CODE_010", targetClass.getSimpleName() + "实例化异常");
        }
    }
}
