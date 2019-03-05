package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.url")
public class SecurityListProperties
{
    private Map<String, String[]> list;
    private String[]              excludePaths = {"/api/**"};
}
