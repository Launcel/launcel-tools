package xyz.launcel.webkit.convert;

import com.google.common.collect.Maps;
import org.springframework.core.convert.converter.Converter;
import xyz.launcel.common.ensure.Me;
import xyz.launcel.menu.BaseEnum;

import java.util.Map;

public class IntegerToEnumConverter<T extends BaseEnum> implements Converter<Integer, T>
{
    private final Map<Integer, T> enumMap = Maps.newHashMap();

    public IntegerToEnumConverter(Class<T> enumType)
    {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums)
        {
            enumMap.put(e.getCode(), e);
        }
    }

    @Override
    public T convert(Integer source)
    {
        T t = enumMap.get(source);
        Me.builder(t).isNull("无法匹配对应的枚举类型");
        return t;
    }
}
