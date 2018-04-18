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
    
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
    
    public static boolean hasBean(String name) {
        return context.containsBean(name);
    }
}
