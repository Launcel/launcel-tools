package xyz.x.webkit.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "web.json-converter")
public class JsonProperties
{
    private Boolean enabled            = true;
    private String  dateFormat         = "LONG_STRING";
    private Double  version            = null;
    private Boolean floatingPointValue = true;
    private Boolean formatPrint        = true;
    private Boolean serializeNull      = true;

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : true;
    }
}
