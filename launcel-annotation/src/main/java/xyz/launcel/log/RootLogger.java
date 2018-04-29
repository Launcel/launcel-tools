package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootLogger {
    
    static Logger log = LoggerFactory.getLogger("root");
    
    private RootLogger() { }
    
    
    public static void INFO(String format, Object... arguments) {
        log.info(format, arguments);
    }
    
    public static void INFO(String msg) {
        log.info(logInfo(msg));
    }
    
    public static void DEBUG(String format, Object... arguments) {
        log.debug(format, arguments);
    }
    
    public static void DEBUG(String msg) {
        log.debug(logInfo(msg));
    }
    
    public static boolean isDebug() {
        return log.isDebugEnabled();
    }
    
    public static void WARN(String format, Object... arguments) {
        log.warn(format, arguments);
    }
    
    public static void WARN(String msg) {
        log.warn(logInfo(msg));
    }
    
    public static void ERROR(String format, Object... arguments) {
        log.error(format, arguments);
    }
    
    public static String logInfo(String msg) {
        return "\n------------------------------------------------------------------------\n\t{}" +
                msg + "\n------------------------------------------------------------------------";
    }
    
}
