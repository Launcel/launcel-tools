package xyz.launcel.exception;

/**
 * Created by xuyang in 2017/10/27
 */
public class ColumnException extends SystemError {
    private static final long serialVersionUID = -2078210171734655804L;
    
    private static String msg = "_DEFINE_ERROR_CODE_007";
    
    public ColumnException(String message) {
        super(message);
    }
    
    public ColumnException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ColumnException(Throwable cause) {
        super(msg, cause);
    }
    
}
