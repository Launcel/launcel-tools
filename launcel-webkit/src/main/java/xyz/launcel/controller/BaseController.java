package xyz.launcel.controller;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.dao.Page;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.exception.SystemException;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.properties.WebTokenProperties;
import xyz.launcel.utils.StringUtils;

import javax.inject.Inject;
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

    protected <T> Page initPaging()
    {
        var pageNoString = request.getParameter("pageNo");
        var rowString    = request.getParameter("row");

        var minIdString = request.getParameter("minId");

        var pageNo = 1;
        var row    = 20;
        var minId  = 0;
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
            if (StringUtils.isNotBlank(minIdString))
            {
                minId = Integer.parseInt(minIdString);
            }
        }
        catch (Exception ignore)
        {
        }
        var page = new Page<>(pageNo, row);
        page.setMinId(minId);
        return page;
    }

    protected String getToken()
    {
        var cookies = getRequest().getCookies();
        if (cookies != null && cookies.length > 0)
        {
            for (var cookie : cookies)
            {
                if (WebTokenProperties.getTokenKey().equals(cookie.getName()))
                {
                    return cookie.getValue();
                }
            }
        }
        throw new ProfessionException("0401");
    }

    protected String getHeaderString(String name)
    {
        if (StringUtils.isBlank(name))
        {
            ExceptionFactory.create("0402");
        }
        var str = getRequest().getHeader(name);
        Me.builder(str).isEmpty("0403");
        return str;
    }
}
