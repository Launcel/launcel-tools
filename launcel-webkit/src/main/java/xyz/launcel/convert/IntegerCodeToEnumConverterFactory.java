package xyz.launcel.convert;

import com.google.common.collect.Maps;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import xyz.launcel.menu.BaseEnum;

import java.util.Map;

public class IntegerCodeToEnumConverterFactory implements ConverterFactory<Integer, BaseEnum>
{
    private static final Map<Class, Converter> converterMap = Maps.newHashMap();

    @Override
    public <T extends BaseEnum> Converter<Integer, T> getConverter(Class<T> targetType)
    {
        Converter<Integer, T> converter = converterMap.get(targetType);
        if (converter == null)
        {
            converter = new IntegerToEnumConverter<>(targetType);
            converterMap.put(targetType, converter);
        }
        return converter;
    }
}
