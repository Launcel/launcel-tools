package xyz.x.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import xyz.x.common.exception.ExceptionHelp;
import xyz.x.log.Log;

public final class SpringBeanUtil implements ApplicationContextAware, InitializingBean
{
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        Log.info("exectute Spring setApplicationContext...");
        synchronized (this)
        {
            if (context == null)
            {
                context = applicationContext;
            }
        }
    }

    @Override
    public void afterPropertiesSet()
    {
        Log.info("exectute Spring afterPropertiesSet...");
        ExceptionHelp.initProperties();
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
