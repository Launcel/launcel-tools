package xyz.launcel.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.var;
import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.BusinessException;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.redis.properties.WebTokenProperties;
import xyz.launcel.utils.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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

    protected String getToken()
    {
        var cookies = getRequest().getCookies();
        if (Objects.isNull(cookies) || cookies.length < 1) {
            throw new BusinessException("0401");
        }
        for (var cookie : cookies) {
            if (WebTokenProperties.getTokenKey().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    protected String getHeaderString(String name)
    {
        if (StringUtils.isBlank(name)) {
            ExceptionFactory.create("0402");
        }
        var str = getRequest().getHeader(name);
        Me.builder(str).isEmpty("0403");
        return str;
    }
}
