package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLogger {
    private Logger log = LoggerFactory.getLogger("root");
    
    protected Logger log() {
        return log;
    }
    
    protected void INFO(String format, Object... arguments) {
        log.info(format, arguments);
    }
    
    protected void INFO(String msg) {
        log.info(logInfo(msg));
    }
    
    protected void DEBUG(String format, Object... arguments) {
        log.debug(format, arguments);
    }
    
    protected void DEBUG(String msg) {
        log.debug(logInfo(msg));
    }
    
    protected boolean isDebug() {
        return log.isDebugEnabled();
    }
    
    protected void WARN(String format, Object... arguments) {
        log.warn(format, arguments);
    }
    
    protected void WARN(String msg) {
        log.warn(logInfo(msg));
    }
    
    protected void ERROR(String format, Object... arguments) {
        log.error(format, arguments);
    }
    
    private String logInfo(String msg) {
        return new StringBuilder("\n------------------------------------------------------------------------\n\t{}").
                append(msg).append("\n------------------------------------------------------------------------").toString();
    }
    
}
