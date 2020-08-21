package xyz.x.jdbc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.x.config.SecurityConfig;
import xyz.launcel.common.exception.ExceptionFactory;
import xyz.x.jdbc.JdbcRole;
import xyz.x.jdbc.SimpleJdbcRole;
import xyz.launcel.log.Log;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.common.utils.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class RoleInterceptor implements HandlerInterceptor
{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        String uri = request.getServletPath();
        if (Log.isDebug())
        {
            Log.debug("request uri is : " + uri);
        }
        HttpSession session = request.getSession(false);
        boolean     flat    = SecurityConfig.isTransit(uri, session);
        if (!flat)
        {
            ExceptionFactory.create("_SECURITY_ERROR_CODE_012", "没有相应的权限");
        }
        return flat;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
    {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        String uri = request.getServletPath();
        if (uri.matches("/login"))
        {
            HttpSession session = request.getSession(false);
            if (session != null)
            {
                Set<String> userRoles = new HashSet<>();
                // find the user role000
                JdbcRole jdbcRole = SimpleJdbcRole.getJdbcRole();
                if (jdbcRole != null)
                {
                    userRoles = jdbcRole.getRoles(session.getAttribute("username").toString());
                }
                userRoles.add("user");
                // do role : save in redis
                if (Log.isDebug())
                {
                    Log.debug("roles is : " + Json.toString(userRoles));
                }
                if (CollectionUtils.isNotEmpty(userRoles))
                {
                    session.setAttribute("role", userRoles);
                }
            }
        }
    }
}
