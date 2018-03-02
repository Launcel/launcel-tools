package xyz.launcel.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.config.SecurityConfig;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.RoleInterceptor;
import xyz.launcel.jdbc.JdbcRole;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.prop.JdbcRolePropertites;
import xyz.launcel.prop.SecurityListProperties;

import java.util.Objects;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(value = {SecurityListProperties.class, JdbcRolePropertites.class})
public class SecurityAutoConfiguration extends WebMvcConfigurerAdapter {
    private final SecurityListProperties securityListProperties;

    private final JdbcRolePropertites jdbcRolePropertites;

    public SecurityAutoConfiguration(SecurityListProperties securityListProperties, JdbcRolePropertites jdbcRolePropertites) {
        this.securityListProperties = securityListProperties;
        this.jdbcRolePropertites = jdbcRolePropertites;
        initSecurityConfig();
    }

    private void initSecurityConfig() {
        if (CollectionUtils.isEmpty(securityListProperties.getList()))
            ExceptionFactory.create("_SECURITY_ERROR_CODE_001", "");
        SecurityConfig.setUris(securityListProperties.getList());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor()).excludePathPatterns(securityListProperties.getExcludePaths());
        super.addInterceptors(registry);
    }

    @Bean
    JdbcRole jdbcRole() {
        if (Objects.isNull(jdbcRolePropertites))
            return null;
        JdbcRole jdbcRole = new JdbcRole();
        jdbcRole.setAuthenticationQuery(jdbcRolePropertites.getAuthenticationQuery());
        jdbcRole.setUserRoleQuery(jdbcRolePropertites.getUserRoleQuery());
        return jdbcRole;
    }
}
