package xyz.launcel.response;

import java.io.Serializable;

/**
 * Created by xuyang in 2017/9/20
 */
public class Response implements Serializable {

    private static final long serialVersionUID = -6522850794196317135L;
    private String code;

    private Object data;

    private String message;

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

    public Response(String code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Response(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Response(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response() {
    }
}
