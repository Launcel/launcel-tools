package xyz.launcel.interceptor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.launcel.config.SecurityConfig;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.ApplicationContextHook;
import xyz.launcel.jdbc.JdbcRole;
import xyz.launcel.lang.Json;
import xyz.launcel.log.BaseLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class RoleInterceptor extends BaseLogger implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getServletPath();
//        if (isDebugEnabled())
        info("\n---------------------------------\n\trequest uri is : {}", uri + "\n---------------------------------");
        HttpSession session = request.getSession(false);
        boolean flat = SecurityConfig.isTransit(uri, session);
        if (!flat)
            ExceptionFactory.create("_SECURITY_ERROR_CODE_001");
        return flat;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String uri = request.getServletPath();
        if (uri.matches("/login")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                @SuppressWarnings("unchecked")
                Set<String> userRoles = new HashSet<>();
                // find the user role
                if (ApplicationContextHook.hasBean("jdbcRole")) {
                    JdbcRole jdbcRole = (JdbcRole) ApplicationContextHook.getBean("jdbcRole");
                    userRoles = jdbcRole.getRoles(session.getAttribute("username").toString());
                }
                userRoles.add("user");
                // do role : save in redis
//                if (isDebugEnabled())
                info("\n---------------------------------\n\troles is : \n{}", Json.toJson(userRoles) + "\n---------------------------------");
                if (CollectionUtils.isNotEmpty(userRoles))
                    session.setAttribute("role", userRoles);
            }
        }
    }
}
