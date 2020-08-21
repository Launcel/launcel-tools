package xyz.x.webkit.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Data
@ConfigurationProperties(prefix = "web.cors")
public class CorsProperties
{
    private Boolean  enabled        = false;
    private String   pathPattern    = "/api/**";
    private String[] methods        = {"GET", "POST", "DELETE", "PUT"};
    private Long     maxAge         = 1800L;
    private String[] allowedOrigins = {"*"};

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : false;
    }
}
