package xyz.launcel.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.dao.Paging;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.response.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public abstract class BaseController extends BaseLogger {

    //    private ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private HttpServletRequest request;

    //    private ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();
    private HttpServletResponse response;

    @ModelAttribute
    protected void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            ExceptionFactory.error("编码转换失败");
        }
        httpServletResponse.setCharacterEncoding("UTF-8");
//        request.set(httpServletRequest);
//        response.set(httpServletResponse);
        request = httpServletRequest;
        response = httpServletResponse;
    }

    protected <T> Paging<T> initPaging() {
//        String pageNoStr = getReqLocal().getParameter("pageNo");
//        String rowStr = getReqLocal().getParameter("maxRow");
        String pageNoStr = getRequest().getParameter("pageNo");
        String rowStr = getRequest().getParameter("maxRow");
//        String lowerIdStr = getRequest().getParameter("lowerId");
//        String largeIdStr = getRequest().getParameter("largeId");

        Integer pageNo = Integer.valueOf(StringUtils.isNotBlank(pageNoStr) ? pageNoStr.trim() : "1");
        Integer maxRow = Integer.valueOf(StringUtils.isNotBlank(rowStr) ? rowStr.trim() : "15");
//        Integer lowerId = Integer.valueOf(StringUtils.isNotBlank(lowerIdStr) ? lowerIdStr.trim() : "1");
//        Integer largeId = Integer.valueOf(StringUtils.isNotBlank(largeIdStr) ? largeIdStr.trim() : "1");
//        return new Paging<>(pageNo, maxRow, lowerId, largeId);
        return new Paging<>(pageNo, maxRow);
    }

//    protected HttpServletRequest getReqLocal() {
//        return request.get();
//    }

//    protected HttpServletResponse getRespLocal() {
//        return response.get();
//    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }

    protected Response getSuccess(Object o) {
        return new Response("1", o);
    }

    protected Response getFail() {
        Response response = new Response();
        response.setCode("-1");
        return response;
    }

    protected Response getFail(String msg) {
        return new Response("-1", msg);
    }


}
