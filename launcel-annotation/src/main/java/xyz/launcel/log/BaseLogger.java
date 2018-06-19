package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLogger {
    private Logger log = LoggerFactory.getLogger("ROOT");

    protected Logger log() {
        return log;
    }

    protected void INFO(String format, Object... arguments) {
        log.info(appender(format), arguments);
    }

    protected void INFO(String msg) {
        log.info(appender(msg));
    }

    protected void DEBUG(String format, Object... arguments) {
        log.debug(appender(format), arguments);
    }

    protected void DEBUG(String msg) {
        log.debug(appender(msg));
    }

    protected boolean isDebug() {
        return log.isDebugEnabled();
    }

    protected void WARN(String format, Object... arguments) {
        log.warn(appender(format), arguments);
    }

    protected void WARN(String msg) {
        log.warn(appender(msg));
    }

    protected void ERROR(String format, Object... arguments) {
        log.error(appender(format), arguments);
    }


    private static String appender(String msg)
    {
        return "\n------------------------------------------------------------------------\n\t" + msg + "\n------------------------------------------------------------------------";
    }
}
