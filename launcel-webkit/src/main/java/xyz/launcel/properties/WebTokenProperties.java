package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web")
public class WebTokenProperties
{
    @Getter
    @Setter
    private static String tokenKey;
}
