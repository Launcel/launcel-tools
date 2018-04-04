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

//    public Response getFail(String message) {
//        setCode("-1");
//        setData(message);
//        return this;
//    }
//
//    public Response getFail() {
//        return this;
//    }
//
//    public Response getSuccess(Object o) {
//        setCode("1");
//        setData(o);
//        return this;
//    }
//
//    public Response getSuccess() {
//        return this;
//    }
}
