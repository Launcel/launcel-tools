package xyz.launcel.log;

/**
 * @author launcel
 */
public class BaseLogger
{

    protected String getName()                          { return RootLogger.getName(); }

    protected void INFO(String format, Object... args)  { RootLogger.INFO(format, args); }

    protected void INFO(String msg)                     { RootLogger.INFO(msg); }

    protected void DEBUG(String format, Object... args) { RootLogger.INFO(format, args); }

    protected void DEBUG(String msg)                    { RootLogger.INFO(msg); }

    protected boolean isDebug()                         { return RootLogger.isDebug(); }

    protected void WARN(String format, Object... args)  { RootLogger.WARN(format, args); }

    protected void WARN(String msg)                     { RootLogger.WARN(msg); }

    protected void ERROR(String format, Object... args) { RootLogger.ERROR(format, args); }

}
