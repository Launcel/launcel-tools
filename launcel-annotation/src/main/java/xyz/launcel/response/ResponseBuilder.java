package xyz.launcel.response;

public class ResponseBuilder
{
    private Boolean isOk    = true;
    private Object  data    = null;
    private String  message = "";
    private String  code    = "0";


    ResponseBuilder builder()
    {
        return new ResponseBuilder();
    }

    public ResponseBuilder isOk(Boolean isOk)
    {
        this.isOk = isOk;
        return this;
    }

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
        return new Response(this.isOk, this.data, this.message, this.code);
    }

}