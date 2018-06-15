package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLogger {
    private Logger log = LoggerFactory.getLogger("ROOT");

    protected Logger log() {
        return log;
    }

    protected void info(String format, Object... arguments) {
        log.info(appender(format), arguments);
    }

    protected void info(String msg) {
        log.info(appender(msg));
    }

    protected void debug(String format, Object... arguments) {
        log.debug(appender(format), arguments);
    }

    protected void debug(String msg) {
        log.debug(appender(msg));
    }

    protected boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    protected void warn(String format, Object... arguments) {
        log.warn(appender(format), arguments);
    }

    protected void warn(String msg) {
        log.warn(appender(msg));
    }

    protected void error(String format, Object... arguments) {
        log.error(appender(format), arguments);
    }


    private static String appender(String msg)
    {
        return "\n------------------------------------------------------------------------\n\t" + msg + "\n------------------------------------------------------------------------";
    }
}
