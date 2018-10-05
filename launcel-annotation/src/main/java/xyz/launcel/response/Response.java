package xyz.launcel.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Launcel in 2017/9/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Response implements Serializable
{

    private static final long serialVersionUID = 5158014804552796478L;

    private Boolean isOk    = true;
    private Object  data    = null;
    private String  message = "";
    private String  code    = "0";

    public static ResponseBuilder builder()
    {
        return new ResponseBuilder();
    }

}
