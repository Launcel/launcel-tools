package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_011
 */
public class SystemException extends RuntimeException
{
    protected SystemException() {
        super();
    }

    SystemException(String message) {
        super(message);
    }

    public SystemException(String code, String msg) {
        this(msg);
        String sb = "\t[" + code + " : " + msg + "]";
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
