package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootLogger {
    
    static Logger log = LoggerFactory.getLogger("ROOT");
    
    private RootLogger() { }
    
    
    public static String getName() { return log.getName(); }
    
    
    public static void INFO(String format, Object... args) { log.info(appenders(format), args); }
    
    public static void INFO(String msg) { log.info(appenders(msg)); }
    
    public static void DEBUG(String format, Object... args) { log.debug(appenders(format), args); }
    
    public static void DEBUG(String msg) { log.debug(appenders(msg)); }
    
    public static boolean isDebug() { return log.isDebugEnabled(); }
    
    public static void WARN(String format, Object... args) { log.warn(appenders(format), args); }
    
    public static void WARN(String msg) { log.warn(appenders(msg)); }
    
    public static void ERROR(String format, Object... args) { log.error(appenders(format), args); }
    
    private static String appenders(String format) {
        return "\n------------------------------------------------------------------------\n\t"
                                                + format +
                "\n------------------------------------------------------------------------";
    }
}
