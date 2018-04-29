package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

public class ProfessionException extends RuntimeException {
    
    private static final long serialVersionUID = -8971428275453038218L;
    
    protected ProfessionException() {
        super();
    }
    
    protected ProfessionException(String message) {
        super(message);
    }
    
    public ProfessionException(String msg, String msgInfo) {
        this(msgInfo);
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        RootLogger.ERROR(sb);
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
