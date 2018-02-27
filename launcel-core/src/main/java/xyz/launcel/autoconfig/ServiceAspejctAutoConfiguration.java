package xyz.launcel.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.launcel.prop.ServiceAspejctProperties;

@ConditionalOnProperty(prefix = "common.aspejct", value = "enabled", havingValue = "true")
@Configuration
public class ServiceAspejctAutoConfiguration {

    @Bean
    public ServiceAspejctProperties serviceAspejctProperties() {
        return new ServiceAspejctProperties();
    }
}
