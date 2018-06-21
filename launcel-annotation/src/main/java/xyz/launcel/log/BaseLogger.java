package xyz.launcel.log;

/**
 * @author launcel
 */
public class BaseLogger
{

    protected String getName()                          { return RootLogger.getName(); }

    protected void info(String format, Object... args)  { RootLogger.info(format, args); }

    protected void info(String msg)                     { RootLogger.info(msg); }

    protected void debug(String format, Object... args) { RootLogger.debug(format, args); }

    protected void debug(String msg)                    { RootLogger.debug(msg); }

    protected boolean isDebug()                         { return RootLogger.isDebug(); }

    protected void warn(String format, Object... args)  { RootLogger.warn(format, args); }

    protected void warn(String msg)                     { RootLogger.warn(msg); }

    protected void error(String msg) { RootLogger.error(msg); }

    protected void error(String format, Object... args) { RootLogger.error(format, args); }

}
