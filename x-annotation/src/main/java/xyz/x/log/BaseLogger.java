package xyz.x.log;

/**
 * @author launcel
 */
public class BaseLogger
{

    protected String getName()                          { return Log.getName(); }

    protected void info(String format, Object... args)  { Log.info(format, args); }

    protected void info(String msg)                     { Log.info(msg); }

    protected void debug(String format, Object... args) { Log.debug(format, args); }

    protected void debug(String msg)                    { Log.debug(msg); }

    protected boolean isDebug()                         { return Log.isDebug(); }

    protected void warn(String format, Object... args)  { Log.warn(format, args); }

    protected void warn(String msg)                     { Log.warn(msg); }

    protected void error(String msg)                    { Log.error(msg); }

    protected void error(String format, Object... args) { Log.error(format, args); }
}
