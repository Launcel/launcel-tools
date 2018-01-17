package xyz.launcel.hook;

import org.springframework.context.ApplicationContext;

public class ApplicationContextHook {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Object getBean(Class<?> clazz) {
        return context.getBean(clazz);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static boolean hasBean(String name) {
        return context.containsBean(name);
    }
}
