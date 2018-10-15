package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_010
 */
public class SystemError extends Error
{
    private static final long serialVersionUID = 2414220843000433011L;

    protected SystemError()
    {
        super();
    }

    SystemError(String msg)
    {
        super(msg);
    }

    public SystemError(String code, String msg)
    {
        this(msg);
        var sb = "[" + code + " : " + msg + "]";
        RootLogger.error(sb);
    }

    SystemError(String message, Throwable cause)
    {
        super(message, cause);
    }

    protected SystemError(Throwable cause)
    {
        super(cause);
    }


}
