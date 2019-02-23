package xyz.launcel.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.launcel.exception.ProfessionException;
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
        return response(message, "-1");
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage(), "-1");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(IllegalArgumentException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage(), "-1");
    }

    @ExceptionHandler(value = ProfessionException.class)
    public Response professionException(ProfessionException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(x.getMessage(), x.getCode());
    }

    @ExceptionHandler(SystemException.class)
    public Response systemError(SystemException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        return response(message, x.getCode());
    }

    private Response response(String str, String code)
    {
        return Response.builder().message(str).code(code).build();
    }

}
