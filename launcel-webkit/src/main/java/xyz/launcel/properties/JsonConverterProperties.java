package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "web.json-converter")
public class JsonConverterProperties
{

    private Boolean enabled    = true;
    private String  dateFormat = "yyyy-MM-dd HH:mm:ss";

}
