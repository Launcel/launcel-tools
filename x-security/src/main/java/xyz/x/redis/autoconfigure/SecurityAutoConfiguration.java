package xyz.x.redis.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.x.config.SecurityConfig;
import xyz.launcel.common.exception.SystemException;
import xyz.x.jdbc.interceptor.RoleInterceptor;
import xyz.x.jdbc.JdbcRole;
import xyz.x.redis.properties.JdbcRolePropertites;
import xyz.x.redis.properties.SecurityListProperties;
import xyz.launcel.common.utils.CollectionUtils;

import java.util.Objects;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(value = {SecurityListProperties.class, JdbcRolePropertites.class})
public class SecurityAutoConfiguration implements WebMvcConfigurer
{
    private final SecurityListProperties securityListProperties;
    private final JdbcRolePropertites    jdbcRolePropertites;

    public SecurityAutoConfiguration(SecurityListProperties securityListProperties, JdbcRolePropertites jdbcRolePropertites)
    {
        this.securityListProperties = securityListProperties;
        this.jdbcRolePropertites = jdbcRolePropertites;
        initSecurityConfig();
    }

    private void initSecurityConfig()
    {
        if (CollectionUtils.isEmpty(securityListProperties.getList()))
        {
            throw new SystemException("_SECURITY_ERROR_CODE_011", "权限路径未配置");
        }
        SecurityConfig.setUris(securityListProperties.getList());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new RoleInterceptor()).excludePathPatterns(securityListProperties.getExcludePaths());
    }

    @Bean
    public JdbcRole jdbcRole()
    {
        if (Objects.isNull(jdbcRolePropertites))
        {
            return null;
        }
        JdbcRole jdbcRole = new JdbcRole();
        jdbcRole.setAuthenticationQuery(jdbcRolePropertites.getAuthenticationQuery());
        jdbcRole.setUserRoleQuery(jdbcRolePropertites.getUserRoleQuery());
        return jdbcRole;
    }
}
