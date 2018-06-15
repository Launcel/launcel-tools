package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLogger {
    private Logger log = LoggerFactory.getLogger("ROOT");

    protected Logger log() {
        return log;
    }

    protected void info(String format, Object... arguments) {
        log.info(format, arguments);
    }

    protected void info(String msg) {
        log.info("\n------------------------------------------------------------------------\n\t{}",
                msg + "\n------------------------------------------------------------------------");
    }

    protected void debug(String format, Object... arguments) {
        log.debug(format, arguments);
    }

    protected void debug(String msg) {
        log.debug("\n------------------------------------------------------------------------\n\t{}",
                msg + "\n------------------------------------------------------------------------");
    }

    protected boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    protected void warn(String format, Object... arguments) {
        log.warn(format, arguments);
    }

    protected void warn(String msg) {
        log.warn(msg);
    }

    protected void error(String format, Object... arguments) {
        log.error(format, arguments);
    }

}
