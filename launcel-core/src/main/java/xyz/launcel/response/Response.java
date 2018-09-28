package xyz.launcel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Launcel in 2017/9/20
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response implements Serializable
{

    private static final long    serialVersionUID = 5158014804552796478L;
    private              Boolean isOk             = true;
    private              Object  data             = null;
    private              String  message;

    public Response(Boolean isOk, Object data)
    {
        this.isOk = isOk;
        this.data = data;
    }


    public Response(String message, Object data)
    {
        this.data = data;
        this.message = message;
    }

    public Response(String message, Boolean isOk)
    {
        this.isOk = isOk;
        this.message = message;
    }

    public Response(Boolean isOk) { this.isOk = isOk; }

}
