package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionFactoryConstant;

@ConfigurationProperties(prefix = SessionFactoryConstant.serviceaAspejctPrefix)
public class ServiceProperties {
    
    private Boolean enabled = false;
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
