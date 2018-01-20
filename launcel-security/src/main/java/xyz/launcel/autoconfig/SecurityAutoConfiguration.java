package xyz.launcel.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.config.SecurityConfig;
import xyz.launcel.interceptor.RoleInterceptor;
import xyz.launcel.prop.SecurityListProperties;

@Configuration
@EnableConfigurationProperties(SecurityListProperties.class)
public class SecurityAutoConfiguration extends WebMvcConfigurerAdapter {
    private final SecurityListProperties securityListProperties;

    public SecurityAutoConfiguration(SecurityListProperties securityListProperties) {
        this.securityListProperties = securityListProperties;
        initSecurityConfig();
    }

    private void initSecurityConfig() {
        SecurityConfig.setUris(securityListProperties.getList());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor()).excludePathPatterns("/api/**");
        super.addInterceptors(registry);
    }
}
