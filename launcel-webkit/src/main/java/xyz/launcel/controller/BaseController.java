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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BaseController extends BaseLogger {

    protected ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();

    protected ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    @ModelAttribute
    protected void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            ExceptionFactory.error("_WEBKIT_ERROR_CODE_002");
        }
        httpServletResponse.setCharacterEncoding("UTF-8");
        request.set(httpServletRequest);
        response.set(httpServletResponse);
    }

    protected <T> Paging<T> initPaging() {
        String pageNoStr = getRequest().getParameter("pageNo");
        String rowStr = getRequest().getParameter("maxRow");
        String lowerIdStr = getRequest().getParameter("lowerId");
        String largeIdStr = getRequest().getParameter("largeId");

        Integer pageNo = Integer.valueOf(StringUtils.isNotBlank(pageNoStr) ? pageNoStr.trim() : "1");
        Integer maxRow = Integer.valueOf(StringUtils.isNotBlank(rowStr) ? rowStr.trim() : "15");
        Integer lowerId = Integer.valueOf(StringUtils.isNotBlank(lowerIdStr) ? lowerIdStr.trim() : "1");
        Integer largeId = Integer.valueOf(StringUtils.isNotBlank(largeIdStr) ? largeIdStr.trim() : "1");
        return new Paging<>(pageNo, maxRow, lowerId, largeId);
    }

    protected HttpServletRequest getRequest() {
        return request.get();
    }

    protected HttpServletResponse getResponse() {
        return response.get();
    }

    protected final Response getSuccess(Object o) {
        return Response.getResponse().setSuccess(o);
    }

    protected final Response getFail() {
        return Response.getResponse().setFail();
    }

}
