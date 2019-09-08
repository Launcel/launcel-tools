package xyz.launcel.exception;

import lombok.Getter;
import lombok.Setter;
import xyz.launcel.log.Log;

@Getter
@Setter
abstract class AbstractException extends RuntimeException
{
    private static final long   serialVersionUID = 3880101935106863166L;
    private              String code;
    private              String meassge;

    AbstractException(String message)
    {
        super(message);
        this.meassge = message;
    }

    AbstractException(String code, String message)
    {
        this(message);
        this.code = code;
        var sb = "[" + code + " : " + message + "]";
        Log.error(sb);
    }

    AbstractException(String message, Throwable cause)
    {
        super(message, cause);
        this.meassge = message;
    }

    AbstractException(String code, String meassge, Throwable cause)
    {
        this(meassge, cause);
        this.code = code;
    }

    AbstractException(Throwable cause)
    {
        super(cause);
    }

    AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    AbstractException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
