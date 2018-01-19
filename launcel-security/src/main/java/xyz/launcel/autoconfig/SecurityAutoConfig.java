package xyz.launcel.autoconfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xyz.launcel.config.SecurityConfig;
import xyz.launcel.interceptor.RoleInterceptor;
import xyz.launcel.prop.SecurityListPropertie;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Configuration
@EnableConfigurationProperties(SecurityListPropertie.class)
public class SecurityAutoConfig extends WebMvcConfigurerAdapter {
    @Inject
    private SecurityListPropertie securityListProperties;

    @PostConstruct
    protected void initSecurityConfig() {
        SecurityConfig.setUris(securityListProperties.getList());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor()).excludePathPatterns("/api/**");
        super.addInterceptors(registry);
    }
}
