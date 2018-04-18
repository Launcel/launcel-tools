package xyz.launcel.exception;

public class SystemError extends Error {
    private static final long serialVersionUID = 2414220843000433011L;
    
    protected SystemError() {
        super();
    }
    
    SystemError(String message) {
        super(message);
    }
    
    SystemError(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected SystemError(Throwable cause) {
        super(cause);
    }
    
    
}
