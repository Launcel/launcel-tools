package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionFactoryConstant;

@Getter
@Setter
@ConfigurationProperties(prefix = SessionFactoryConstant.serviceaAspejctPrefix)
public class ServiceProperties
{
    private Boolean enabled = false;
}
