package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLogger {
    private Logger log = LoggerFactory.getLogger(this.getClass());

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
        log.debug(msg);
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

//    protected static void Info(String format, Object... arguments) {
//        StaticLog.info(format, arguments);
//    }
//
//    protected static void Info(String msg) {
//        StaticLog.info(msg);
//    }
//
//    protected static boolean debugEnabled() {
//        return StaticLog.isDebugEnabled();
//    }
//
//    protected static void Debug(String format, Object... arguments) {
//        StaticLog.debug(format, arguments);
//    }
//
//    protected static void Debug(String msg) {
//        StaticLog.debug(msg);
//    }
//
//    protected static void Warn(String format, Object... arguments) {
//        StaticLog.warn(format, arguments);
//    }
//
//    protected static void Warn(String msg) {
//        StaticLog.warn(msg);
//    }
//
//    protected static void Error(String format, Object... arguments) {
//        StaticLog.error(format, arguments);
//    }
//
//    private static final class StaticLog {
//
//        private static final Logger staticLog = LoggerFactory.getLogger(BaseLogger.class);
//
//        protected static Logger log() {
//            return staticLog;
//        }
//
//        protected static void info(String format, Object... arguments) {
//            staticLog.info(format, arguments);
//        }
//
//        protected static void info(String msg) {
//            staticLog.info("\n------------------------------------------------------------------------\n\t{}",
//                    msg + "\n------------------------------------------------------------------------");
//        }
//
//        protected static void debug(String format, Object... arguments) {
//            staticLog.debug(format, arguments);
//        }
//
//        protected static void debug(String msg) {
//            staticLog.debug(msg);
//        }
//
//        protected static boolean isDebugEnabled() {
//            return staticLog.isDebugEnabled();
//        }
//
//        protected static void warn(String format, Object... arguments) {
//            staticLog.warn(format, arguments);
//        }
//
//        protected static void warn(String msg) {
//            staticLog.warn(msg);
//        }
//
//        protected static void error(String format, Object... arguments) {
//            staticLog.error(format, arguments);
//        }
//    }

}
