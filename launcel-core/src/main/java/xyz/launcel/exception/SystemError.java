package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

public class SystemError extends Error {
    private static final long serialVersionUID = 2414220843000433011L;
    
    protected SystemError() {
        super();
    }
    
    protected SystemError(String message) {
        super(message);
    }
    
    public SystemError(String msg, String msgInfo) {
        this(msgInfo);
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        RootLogger.ERROR(sb);
    }
    
    SystemError(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected SystemError(Throwable cause) {
        super(cause);
    }
    
    
}
