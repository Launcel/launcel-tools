package xyz.launcel.exception;

public class ProfessionException extends RuntimeException {

    private static final long serialVersionUID = -8971428275453038218L;

    protected ProfessionException() {
        super();
    }

    ProfessionException(String message) {
        super(message);
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
