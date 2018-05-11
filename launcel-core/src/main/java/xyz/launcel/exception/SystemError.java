package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

public class SystemError extends Error {
    private static final long serialVersionUID = 2414220843000433011L;
    
    protected SystemError() {
        super();
    }
    
    protected SystemError(String msg) {
        super(msg);
    }
    
    public SystemError(String code, String msg) {
        this(msg);
        String sb = "\t[" + code + " : " + msg + "]";
        RootLogger.ERROR(sb);
    }
    
    SystemError(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected SystemError(Throwable cause) {
        super(cause);
    }
    
    
}
