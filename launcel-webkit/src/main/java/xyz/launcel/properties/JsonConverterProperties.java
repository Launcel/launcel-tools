package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.json.builder.DefaultGson;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.json-converter")
public class JsonConverterProperties
{
    private Boolean                enabled    = true;
    private DefaultGson.DateFormat dateFormat = DefaultGson.DateFormat.LONG_STRING;
    private Double                 version    = null;
}
