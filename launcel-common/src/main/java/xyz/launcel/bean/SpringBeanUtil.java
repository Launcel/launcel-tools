package xyz.launcel.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

public final class SpringBeanUtil
{
    private static ApplicationContext context;

    public static void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        if (context == null)
        {
            context = applicationContext;
        }
    }

    public static ApplicationContext getContext()
    {
        return context;
    }

    @NonNull
    public static <T> T getBean(@NonNull Class<T> clazz)
    {
        return context.getBean(clazz);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T getBean(@NonNull String name)
    {
        return (T) context.getBean(name);
    }

    public static boolean hasBean(@NonNull String name)
    {
        return context.containsBean(name);
    }
}
