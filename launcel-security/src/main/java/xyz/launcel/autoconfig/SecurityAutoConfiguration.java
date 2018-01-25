package xyz.launcel.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.config.SecurityConfig;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.RoleInterceptor;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.prop.SecurityListProperties;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(SecurityListProperties.class)
public class SecurityAutoConfiguration extends WebMvcConfigurerAdapter {
    private final SecurityListProperties securityListProperties;

    public SecurityAutoConfiguration(SecurityListProperties securityListProperties) {
        this.securityListProperties = securityListProperties;
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
}
