package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.cors")
public class CorsProperties
{

    private Boolean  enabled        = false;
    private String   pathPattern    = "/api/**";
    private String[] methods        = {"GET", "POST", "DELETE", "PUT"};
    private Long     maxAge         = 1800L;
    private String[] allowedOrigins = {"*"};
}
