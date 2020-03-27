package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ConfigurationProperties(prefix = SessionFactoryConstant.serviceaAspejctPrefix)
public class ServiceProperties
{
    private Boolean enabled = false;
}
