package xyz.launcel.session.web.http;

import lombok.var;
import org.springframework.session.web.http.DefaultCookieSerializer;
import xyz.launcel.utils.Base64;
import xyz.launcel.utils.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimyDefaultCookieSerializer extends DefaultCookieSerializer
{

    private String cookieName = "SESSION";

    private Boolean useSecureCookie;

    private boolean useHttpOnlyCookie = isServlet3();

    private String cookiePath;

    private int cookieMaxAge = -1;

    private String domainName;

    private Pattern domainNamePattern;

    private String jvmRoute;

    private boolean useBase64Encoding;

    private String rememberMeRequestAttribute;

    public List<String> readCookieValues(HttpServletRequest request)
    {
        var cookies              = request.getCookies();
        var matchingCookieValues = new ArrayList<String>();
        if (cookies != null)
        {
            for (var cookie : cookies)
            {
                if (this.cookieName.equals(cookie.getName()))
                {
                    var sessionId = this.useBase64Encoding ? Base64.decode(cookie.getValue()) : cookie.getValue();
                    if (sessionId == null)
                    {
                        continue;
                    }
                    if (this.jvmRoute != null && sessionId.endsWith(this.jvmRoute))
                    {
                        sessionId = sessionId.substring(0, sessionId.length() - this.jvmRoute.length());
                    }
                    matchingCookieValues.add(sessionId);
                }
            }
        }
        return matchingCookieValues;
    }

    public void writeCookieValue(CookieValue cookieValue)
    {
        var request  = cookieValue.getRequest();
        var response = cookieValue.getResponse();

        var requestedCookieValue = cookieValue.getCookieValue();
        var actualCookieValue    = this.jvmRoute == null ? requestedCookieValue : requestedCookieValue + this.jvmRoute;

        var sessionCookie = new Cookie(this.cookieName, this.useBase64Encoding ? Base64.encode(actualCookieValue) : actualCookieValue);
        sessionCookie.setSecure(isSecureCookie(request));
        sessionCookie.setPath(getCookiePath());
        var domainName = getDomainName(request);
        if (domainName != null)
        {
            sessionCookie.setDomain(domainName);
        }

        if (this.useHttpOnlyCookie)
        {
            sessionCookie.setHttpOnly(true);
        }

        if ("".equals(requestedCookieValue))
        {
            sessionCookie.setMaxAge(0);
        }
        else if (this.rememberMeRequestAttribute != null && request.getAttribute(this.rememberMeRequestAttribute) != null)
        {
            sessionCookie.setMaxAge(Integer.MAX_VALUE);
        }
        else
        {
            sessionCookie.setMaxAge(this.cookieMaxAge);
        }

        response.addCookie(sessionCookie);
    }

    public void setUseSecureCookie(boolean useSecureCookie)
    {
        this.useSecureCookie = useSecureCookie;
    }

    public void setUseHttpOnlyCookie(boolean useHttpOnlyCookie)
    {
        if (useHttpOnlyCookie && !isServlet3())
        {
            throw new IllegalArgumentException("You cannot set useHttpOnlyCookie to true in pre Servlet 3 environment");
        }
        this.useHttpOnlyCookie = useHttpOnlyCookie;
    }

    private boolean isSecureCookie(HttpServletRequest request)
    {
        if (this.useSecureCookie == null)
        {
            return request.isSecure();
        }
        return this.useSecureCookie;
    }

    public void setCookiePath(String cookiePath)
    {
        this.cookiePath = cookiePath;
    }

    public void setCookieName(String cookieName)
    {
        if (cookieName == null)
        {
            throw new IllegalArgumentException("cookieName cannot be null");
        }
        this.cookieName = cookieName;
    }

    public void setCookieMaxAge(int cookieMaxAge)
    {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setDomainName(String domainName)
    {
        if (this.domainNamePattern != null)
        {
            throw new IllegalStateException("Cannot set both domainName and domainNamePattern");
        }
        this.domainName = domainName;
    }

    public void setDomainNamePattern(String domainNamePattern)
    {
        if (this.domainName != null)
        {
            throw new IllegalStateException("Cannot set both domainName and domainNamePattern");
        }
        this.domainNamePattern = Pattern.compile(domainNamePattern, Pattern.CASE_INSENSITIVE);
    }

    public void setJvmRoute(String jvmRoute)
    {
        this.jvmRoute = "." + jvmRoute;
    }

    public void setUseBase64Encoding(boolean useBase64Encoding)
    {
        this.useBase64Encoding = useBase64Encoding;
    }

    public void setRememberMeRequestAttribute(String rememberMeRequestAttribute)
    {
        if (rememberMeRequestAttribute == null)
        {
            throw new IllegalArgumentException("rememberMeRequestAttribute cannot be null");
        }
        this.rememberMeRequestAttribute = rememberMeRequestAttribute;
    }

    private String getDomainName(HttpServletRequest request)
    {
        if (this.domainName != null)
        {
            return this.domainName;
        }
        if (this.domainNamePattern != null)
        {
            var matcher = this.domainNamePattern.matcher(request.getServerName());
            if (matcher.matches())
            {
                return matcher.group(1);
            }
        }
        return null;
    }

    private boolean isServlet3()
    {
        try
        {
            ServletRequest.class.getMethod("startAsync");
            return true;
        }
        catch (NoSuchMethodException ignored)
        {
        }
        return false;
    }

    private String getCookiePath()
    {
        if (StringUtils.isBlank(cookiePath))
            return "/";
        return this.cookiePath;
    }
}
