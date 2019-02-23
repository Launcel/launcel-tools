package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.json-converter")
public class JsonProperties
{
    private Boolean enabled            = true;
    private String  dateFormat         = "LONG_STRING";
    private Double  version            = null;
    private Boolean floatingPointValue = true;
    private Boolean formatPrint        = true;
    private Boolean serializeNull      = true;
}
