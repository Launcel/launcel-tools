package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.aspejct")
public class WebAspejctProperties
{
    public static String tokenKey;
    private Boolean enabled = false;
}
