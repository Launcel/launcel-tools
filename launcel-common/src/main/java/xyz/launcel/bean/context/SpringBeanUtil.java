package xyz.launcel.bean.context;

import org.springframework.context.ApplicationContext;

public final class SpringBeanUtil
{
    private static ApplicationContext context;

    static void setApplicationContext(ApplicationContext applicationContext)
    {
        context = applicationContext;
    }

    public static ApplicationContext getContext()
    {
        return context;
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return context.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name)
    {
        return (T) context.getBean(name);
    }

    public static boolean hasBean(String name)
    {
        return context.containsBean(name);
    }
}
