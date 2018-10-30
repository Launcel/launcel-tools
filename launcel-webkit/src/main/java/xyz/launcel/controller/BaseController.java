package xyz.launcel.controller;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.dao.Page;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.properties.WebAspejctProperties;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter(value = AccessLevel.PROTECTED)
public abstract class BaseController extends BaseLogger
{
    @Inject
    private HttpServletRequest  request;
    @Inject
    private HttpServletResponse response;

    @ModelAttribute
    public void init()
    {
        getResponse().setContentType("text/html;charset=utf-8");
    }


    protected <T> Page<T> initPaging()
    {
        String pageNoString = getRequest().getParameter("pageNo");
        String rowString    = getRequest().getParameter("row");

        int pageNo = 1;
        int row    = 20;
        try
        {
            if (StringUtils.isNotBlank(pageNoString))
            {
                pageNo = Integer.valueOf(pageNoString.trim());
            }
            if (StringUtils.isNotBlank(rowString))
            {
                row = Integer.valueOf(rowString.trim());
            }
        }
        catch (Exception ignore) { }
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

}
