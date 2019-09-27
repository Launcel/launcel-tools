package xyz.launcel.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.exception.BusinessException;
import xyz.launcel.exception.SystemException;
import xyz.launcel.log.Log;
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
    public Response throwable(final Throwable x)
    {
        Log.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(final NullPointerException x)
    {
        Log.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(final IllegalArgumentException x)
    {
        Log.error("error info : {}", x.getCause());
        output(x);
        return response(message, "-1");
    }

    @ExceptionHandler(value = BusinessException.class)
    public Response professionException(final BusinessException x)
    {
        Log.error("error info : {}", x.getCause());
        output(x);
        return response(x.getMessage(), x.getCode());
    }

    @ExceptionHandler(SystemException.class)
    public Response systemError(final SystemException x)
    {
        Log.error("error info : {}", x.getCause());
        output(x);
        return response(x.getMeassge(), x.getCode());
    }

    //    @ExceptionHandler(ValidationException.class)
    //    public Response validationException(final ValidationException x)
    //    {
    //        Log.error("error info : {}", x.getCause());
    //        output(x);
    //        var code    = x.getMessage();
    //        var message = ExceptionHelp.getMessage(code);
    //        return response(message.get(code), "-1");
    //    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response constraintViolationException(final ConstraintViolationException x)
    {
        var code = x.getConstraintViolations();
        var sb   = new StringBuilder();
        code.forEach(c -> sb.append(c.getMessage()));
        var message = ExceptionHelp.getMessage(sb.toString());
        System.out.printf("=========\n\terror info : %s", sb.toString());
        output(x);
        return response(message.get(sb.toString()), "-1");
    }

    private void output(final Throwable x)
    {
        x.printStackTrace();
    }

    private Response response(final String str, final String code)
    {
        return Response.builder().message(str).code(code).build();
    }
}
