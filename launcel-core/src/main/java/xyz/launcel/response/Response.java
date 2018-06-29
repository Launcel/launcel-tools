package xyz.launcel.response;

import java.io.Serializable;

/**
 * Created by Launcel in 2017/9/20
 */
public class Response implements Serializable
{

    private static final long    serialVersionUID = 5158014804552796478L;
    private              Boolean isOk             = true;
    private              Object  data             = null;
    private              String  message;

    public Boolean getIsOk()               { return isOk; }

    public Response setIsOk(Boolean ok)        { isOk = ok; return this; }

    public Object getData()                { return data; }

    public Response setData(Object object)     { this.data = object; return this; }

    public String getMessage()             { return message; }

    public Response setMessage(String message) { this.message = message; return this;}

    public Response(Boolean isOk, Object data, String message)
    {
        this.isOk = isOk;
        this.data = data;
        this.message = message;
    }

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

    public Response()             { }
}
