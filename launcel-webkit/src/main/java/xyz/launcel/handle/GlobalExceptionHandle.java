package xyz.launcel.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.exception.SystemError;
import xyz.launcel.lang.Json;
import xyz.launcel.response.Response;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xuyang in 2017/9/22
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException e) {
        return response(e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(IllegalArgumentException x) {
        return response(x.getMessage());
    }

    @ExceptionHandler(value = ProfessionException.class)
    public Response professionException(ProfessionException x) {
        return responseInfo(x.getMessage());
    }
    //        getLog().error("missingServletRequestParameterException---Host {} ERROR: {}", req.getRemoteHost(), e.getLocalizedMessage());

    @ExceptionHandler(value = SQLException.class)
    public Response sqlException(SQLException x) {
        x.printStackTrace();
        return response("系统内服错误!");
    }

    @ExceptionHandler(value = IOException.class)
    public Response ioException(IOException x) {
        x.printStackTrace();
        return response("系统内服错误!");
    }

    @ExceptionHandler(SystemError.class)
    public Response systemError(Error x) {
        x.printStackTrace();
        return response("系统内服错误!");
    }

    private Response response(String str) {
        Response response = new Response();
        response.setCode("-1");
        response.setMessage(str);
        return response;
    }

    private Response responseInfo(String str) {
        return Json.parseObject(str, Response.class);
    }
}
