package xyz.launcel.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootLogger
{

    private static Logger log = LoggerFactory.getLogger("ROOT");

    private RootLogger() { }


    public static String getName()
    {
        return log.getName();
    }


    public static void INFO(String format, String... arguments)
    {
        log.info(format, arguments);
    }

    public static void INFO(String msg)
    {
        log.info(appender(msg));
    }

    public static void DEBUG(String format, String... arguments)
    {
        log.debug(appender(format), arguments);
    }

    public static void DEBUG(String msg)
    {
        log.debug(appender(msg));
    }

    public static boolean isDebug()
    {
        return log.isDebugEnabled();
    }

    public static void WARN(String format, String... arguments)
    {
        log.warn(appender(format), arguments);
    }

    public static void WARN(String msg)
    {
        log.warn(appender(msg));
    }

    public static void ERROR(String msg) { log.error(appender(msg));}

    public static void ERROR(String format, String... arguments)
    {
        log.error(appender(format), arguments);
    }

    private static String appender(String msg)
    {
        return "\n------------------------------------------------------------------------\n\t{}" + msg + "\n------------------------------------------------------------------------";
    }

}
