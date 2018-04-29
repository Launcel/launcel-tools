package xyz.launcel.log;

public class BaseLogger {
    
    protected void INFO(String format, Object... arguments) {
        RootLogger.INFO(format, arguments);
    }
    
    protected void INFO(String msg) {
        RootLogger.INFO(msg);
    }
    
    protected void DEBUG(String format, Object... arguments) {
        RootLogger.INFO(format, arguments);
    }
    
    protected void DEBUG(String msg) {
        RootLogger.INFO(msg);
    }
    
    protected boolean isDebug() {
        return RootLogger.isDebug();
    }
    
    protected void WARN(String format, Object... arguments) {
        RootLogger.WARN(format, arguments);
    }
    
    protected void WARN(String msg) {
        RootLogger.WARN(msg);
    }
    
    protected void ERROR(String format, Object... arguments) {
        RootLogger.ERROR(format, arguments);
    }
    
}
