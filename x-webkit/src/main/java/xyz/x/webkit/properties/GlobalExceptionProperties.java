package xyz.x.webkit.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "web.global-exception")
public class GlobalExceptionProperties
{
    private Boolean enabled = true;

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : true;
    }
}
