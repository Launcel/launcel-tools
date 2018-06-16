package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_011
 */
public class SystemException extends RuntimeException
{
    private static final long serialVersionUID = -1597189035906555024L;

    protected SystemException() {
        super();
    }

    SystemException(String message) {
        super(message);
    }

    public SystemException(String code, String msg) {
        this(msg);
        String sb = "[" + code + " : " + msg + "]";
        RootLogger.ERROR(sb);
    }

    protected SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    protected SystemException(Throwable cause) {
        super(cause);
    }

    protected SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
