package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionConstant;

@ConfigurationProperties(prefix = SessionConstant.serviceaAspejctPrefix)
public class ServiceProperties {

    private Boolean enabled = false;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
