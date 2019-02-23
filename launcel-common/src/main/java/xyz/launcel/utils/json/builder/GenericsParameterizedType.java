package xyz.launcel.utils.json.builder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericsParameterizedType implements ParameterizedType
{
    private Class<?> clazz;

    public GenericsParameterizedType(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    public Type[] getActualTypeArguments()
    {
        return new Type[]{clazz};
    }

    @Override
    public Type getRawType()
    {
        return List.class;
    }

    @Override
    public Type getOwnerType()
    {
        return null;
    }
}
