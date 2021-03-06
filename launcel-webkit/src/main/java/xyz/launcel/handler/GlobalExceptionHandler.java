package xyz.launcel.handler;

import lombok.var;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.exception.SystemException;
import xyz.launcel.log.RootLogger;
import xyz.launcel.response.Response;

import javax.validation.ConstraintViolationException;

/**
 * Created by xuyang in 2017/9/22
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{

    private final String message = "网络错误！";

    @ExceptionHandler(value = Throwable.class)
    public Response throwable(Throwable x)
    {
        RootLogger.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(IllegalArgumentException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = ProfessionException.class)
    public Response professionException(ProfessionException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        output(x);
        return response(x.getMessage(), x.getCode());
    }

    @ExceptionHandler(SystemException.class)
    public Response systemError(SystemException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        output(x);
        return response(x.getMeassge(), x.getCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response violationException(ConstraintViolationException x)
    {
        RootLogger.error("error info : {}", x.getCause());
        var cves = x.getConstraintViolations();
        var sb   = new StringBuilder();
        cves.forEach(c -> sb.append(c.getMessage()));
        output(x);
        return response(sb.toString(), "-1");
    }

    private void output(Throwable x) {
        x.printStackTrace();
    }

    private Response response(String str, String code)
    {
        return Response.builder().message(str).code(code).build();
    }
}
