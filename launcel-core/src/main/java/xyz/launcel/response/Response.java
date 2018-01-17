package xyz.launcel.response;

/**
 * Created by xuyang in 2017/9/20
 */
public class Response {

    private String code;

    private Object data;

    private String message;

    public static Response getResponse() {
        return new Response();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object object) {
        this.data = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response setFail(String message) {
        setCode("-1");
        setData(message);
        setMessage("FAIL");
        return this;
    }

    public Response setFail() {
        return setFail(null);
    }

    public Response setSuccess(Object o) {
        setCode("1");
        setData(o);
        setMessage("SUCCESS");
        return this;
    }

    public Response setSuccess() {
        return setSuccess(null);
    }
}
