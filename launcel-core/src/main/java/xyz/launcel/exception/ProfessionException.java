package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_012
 */
public class ProfessionException extends RuntimeException {
    
    private static final long serialVersionUID = -8971428275453038218L;
    
    protected ProfessionException() {
        super();
    }
    
    ProfessionException(String message) {
        super(message);
    }
    
    public ProfessionException(String code, String msg) {
        this(msg);
        String sb = "[" + code + " : " + msg + "]";
        RootLogger.error(sb);
    }
    
    protected ProfessionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected ProfessionException(Throwable cause) {
        super(cause);
    }
    
    protected ProfessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
