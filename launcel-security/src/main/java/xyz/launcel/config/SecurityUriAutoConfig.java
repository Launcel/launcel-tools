package xyz.launcel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "launcel.security")
public class SecurityUriAutoConfig {
    private Map<String, String[]> filter;

    public Map<String, String[]> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String[]> filter) {
        this.filter = filter;
    }
}
