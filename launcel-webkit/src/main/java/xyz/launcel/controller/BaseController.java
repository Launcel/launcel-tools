package xyz.launcel.controller;

import xyz.launcel.dao.Paging;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.response.Response;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController extends BaseLogger {

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private HttpServletResponse httpServletResponse;

    protected <T> Paging<T> initPaging() {
        String pageNoStr = httpServletRequest.getParameter("pageNo");
        String rowStr = httpServletRequest.getParameter("maxRow");

        Integer pageNo = Integer.valueOf(StringUtils.isNotBlank(pageNoStr) ? pageNoStr.trim() : "1");
        Integer maxRow = Integer.valueOf(StringUtils.isNotBlank(rowStr) ? rowStr.trim() : "15");
        return new Paging<>(pageNo, maxRow);
    }

    protected final HttpServletRequest getRequest() {
        return this.httpServletRequest;
    }

    protected final HttpServletResponse getResponse() {
        return this.httpServletResponse;
    }

    protected Response getSuccess(Object o) {
        return new Response(true, o);
    }

    protected Response getFail() {
        Response response = new Response();
        response.setIsOk(false);
        return response;
    }

    protected Response getFail(String msg) {
        return new Response(false, msg);
    }

}
