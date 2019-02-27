package xyz.launcel.bean;

import org.springframework.context.ApplicationContext;
import xyz.launcel.log.RootLogger;

public final class SpringBeanUtil
{
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext)
    {
        if (context == null)
        {
            RootLogger.debug("init ApplicationContext... ");
            RootLogger.debug(applicationContext.toString());
            context = applicationContext;
        }
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
