package xyz.launcel.webkit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.var;
import org.springframework.web.bind.annotation.ModelAttribute;
import xyz.launcel.common.ensure.Predict;
import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.StringUtils;
import xyz.launcel.webkit.properties.WebTokenProperties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Getter(value = AccessLevel.PROTECTED)
public abstract class BaseController
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
        if (Objects.isNull(cookies) || cookies.length < 1)
        {
            ExceptionFactory.create("0401");
        }
        for (var cookie : cookies)
        {
            if (WebTokenProperties.getTokenKey().equals(cookie.getName()))
            {
                return cookie.getValue();
            }
        }
        return null;
    }

    protected String getHeaderString(String name)
    {
        if (StringUtils.isBlank(name))
        {
            ExceptionFactory.create("0402");
        }
        var str = getRequest().getHeader(name);
        Predict.builder(str).isEmpty("0403");
        return str;
    }
}
