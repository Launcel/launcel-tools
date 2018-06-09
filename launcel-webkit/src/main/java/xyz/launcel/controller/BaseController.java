package xyz.launcel.controller;

import xyz.launcel.dao.Page;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.response.Response;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController extends BaseLogger
{

    @Inject
    private HttpServletRequest HttpRequest;

    @Inject
    private HttpServletResponse HttpResponse;


    protected <T> Page<T> initPaging()
    {
        String pageNoStr = getRequest().getParameter("pageNo");
        String rowStr    = getRequest().getParameter("maxRow");

        Integer pageNo = StringUtils.isNotBlank(pageNoStr) ? Integer.valueOf(pageNoStr.trim()) : 1;
        Integer maxRow = StringUtils.isNotBlank(rowStr) ? Integer.valueOf(rowStr.trim()) : Integer.MAX_VALUE;
        //noinspection unchecked
        return new Page<>(pageNo, maxRow);
    }

    protected HttpServletRequest getRequest()
    {
        return HttpRequest;
    }

    protected HttpServletResponse getResponse()
    {
        return HttpResponse;
    }

    protected Response getSuccess()
    {
        return new Response(true);
    }

    protected Response getSuccess(Object o)
    {
        return new Response(true, o);
    }

    protected Response getSuccess(String msg)
    {
        return new Response(msg, true);
    }

    protected Response getFail()
    {
        Response response = new Response();
        response.setIsOk(false);
        return response;
    }

    protected Response getFail(String msg)
    {
        return new Response(false, msg);
    }

}
