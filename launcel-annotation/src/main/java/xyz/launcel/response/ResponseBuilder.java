package xyz.launcel.response;

import java.util.Date;

public class ResponseBuilder
{
    private Object data    = null;
    private String message = "";
    private String code    = "0";

    public ResponseBuilder data(Object data)
    {
        this.data = data;
        return this;
    }

    public ResponseBuilder message(String message)
    {
        this.message = message;
        return this;
    }

    public ResponseBuilder code(String code)
    {
        this.code = code;
        return this;
    }

    public Response build()
    {
        return new Response(this.data, this.message, this.code, new Date().getTime());
    }

}