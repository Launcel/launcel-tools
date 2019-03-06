package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author launcel
 */
public class RootLogger
{

    private static Logger log = LoggerFactory.getLogger("ROOT");

    private RootLogger()                                    { }

    public static String getName()                          { return log.getName(); }

    public static void info(String format, Object... args)  { log.info(appenders(format), args); }

    public static void info(String msg)                     { log.info(appenders(msg)); }

    public static void debug(String format, Object... args) { log.debug(appenders(format), args); }

    public static void debug(String msg)                    { log.debug(appenders(msg)); }

    public static boolean isDebug()                         { return log.isDebugEnabled(); }

    public static void warn(String format, Object... args)  { log.warn(appenders(format), args); }

    public static void warn(String msg)                     { log.warn(appenders(msg)); }

    public static void error(String msg)                    { log.error(appenders(msg));}

    public static void error(String format, Object... args) { log.error(appenders(format), args); }

    public static void error(String msg, Throwable error)   { log.error(msg, error); }

    private static String appenders(String format)
    {
        return "\n------------------------------------------------------------------------\n\t"
                + format + "\n------------------------------------------------------------------------";
    }
}
