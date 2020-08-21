package xyz.launcel.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Launcel in 2017/9/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Response implements Serializable
{
    private static final long serialVersionUID = 5158014804552796478L;

    private Object data;
    private String message;
    private String code;
    private Long   timestrap;

    public Response(String message, String code)
    {
        this.message = message;
        this.code = code;
    }

    public Response(Object data, String message, String code)
    {
        this.data = data;
        this.message = message;
        this.code = code;
        timestrap = new Date().getTime();
    }

    public static ResponseBuilder builder()
    {
        return new ResponseBuilder();
    }
}
