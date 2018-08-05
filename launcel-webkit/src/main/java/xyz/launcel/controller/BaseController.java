package xyz.launcel.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.dao.Page;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.properties.WebAspejctProperties;
import xyz.launcel.response.Response;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController extends BaseLogger
{

    @Inject
    private HttpServletRequest HttpRequest;

    @Inject
    private HttpServletResponse HttpResponse;

    @ModelAttribute
    public void init()
    {
        getResponse().setContentType("text/html;charset=utf-8");
    }


    protected <T> Page<T> initPaging()
    {
        String pageNoString = getRequest().getParameter("pageNo");
        String rowString    = getRequest().getParameter("row");

        Integer pageNo;
        try
        {
            pageNo = StringUtils.isNotBlank(pageNoString) ? Integer.valueOf(pageNoString.trim()) : 1;
        }
        catch (Exception x)
        {
            pageNo = 1;
        }
        Integer row;
        try
        {
            row = StringUtils.isNotBlank(rowString) ? Integer.valueOf(rowString.trim()) : 20;
        }
        catch (Exception x)
        {
            row = 20;
        }
        return new Page<>(pageNo, row);
    }

    protected String getToken()
    {
        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null && cookies.length > 0)
        {
            for (Cookie cookie : cookies)
            {
                if (WebAspejctProperties.tokenKey.equals(cookie.getName()))
                {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    protected String getHeaderString(String name)
    {
        return getRequest().getHeader(name);
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

    protected Response getSuccess(String msg)
    {
        return new Response(msg, true);
    }

    protected Response getFail()
    {
        return new Response(false);
    }

    protected Response getFail(String msg)
    {
        return new Response(msg, false);
    }

}
