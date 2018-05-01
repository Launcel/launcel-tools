package xyz.launcel.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.exception.SystemError;
import xyz.launcel.log.RootLogger;
import xyz.launcel.response.Response;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by xuyang in 2017/9/22
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle {
    
    @ExceptionHandler(value = Throwable.class)
    public Response throwable(Throwable x) {
        x.printStackTrace();
        return response(x.getMessage());
    }
    
    @ExceptionHandler(value = Exception.class)
    public Response exception(Exception x) {
        x.printStackTrace();
        return response(x.getMessage());
    }
    
    @ExceptionHandler(value = NullPointerException.class)
    public Response nullPointerException(NullPointerException x) {
        x.printStackTrace();
        return response(x.getMessage());
    }
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response illegalArgumentException(IllegalArgumentException x) {
        x.printStackTrace();
        return response(x.getMessage());
    }
    
    @ExceptionHandler(value = ProfessionException.class)
    public Response professionException(ProfessionException x) {
        return response(x.getMessage());
    }
    
    @ExceptionHandler(value = SQLException.class)
    public Response sqlException(SQLException x) {
        x.printStackTrace();
        return response("系统内部错误!");
    }
    
    @ExceptionHandler(value = IOException.class)
    public Response ioException(IOException x) {
        x.printStackTrace();
        return response("系统内部错误!");
    }
    
    @ExceptionHandler(SystemError.class)
    public Response systemError(Error x) {
        x.printStackTrace();
        return response("系统内部错误!");
    }
    
    private Response response(String str) {
        return new Response(false, str);
    }
    
}
