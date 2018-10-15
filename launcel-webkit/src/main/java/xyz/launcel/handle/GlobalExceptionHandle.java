package xyz.launcel.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.exception.SystemError;
import xyz.launcel.exception.SystemException;
import xyz.launcel.log.RootLogger;
import xyz.launcel.response.Response;

/**
 * Created by xuyang in 2017/9/22
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle
{

    private final String message = "网络错误！";

    @ExceptionHandler(value = Throwable.class)
    public Response throwable(Throwable x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(message);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(IllegalArgumentException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage());
    }

    @ExceptionHandler(value = SystemException.class)
    public Response systemException(SystemException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(message);
    }

    @ExceptionHandler(value = ProfessionException.class)
    public Response professionException(ProfessionException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage());
    }

    @ExceptionHandler(SystemError.class)
    public Response systemError(Error x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(message);
    }

    private Response response(String str)
    {
        return Response.builder().message(str).code("-1").build();
    }

}
