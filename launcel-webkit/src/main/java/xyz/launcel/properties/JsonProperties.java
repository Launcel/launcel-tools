package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.json.builder.DefaultGsonBuilder;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.json-converter")
public class JsonProperties
{
    private Boolean                       enabled            = true;
    private DefaultGsonBuilder.DateFormat dateFormat         = DefaultGsonBuilder.DateFormat.LONG_STRING;
    private Double                        version            = null;
    private Boolean                       floatingPointValue = true;
    private Boolean                       formatPrint        = true;
    private Boolean                       serializeNull      = true;
}
