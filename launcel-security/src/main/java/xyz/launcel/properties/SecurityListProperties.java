package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "web.security")
public class SecurityListProperties {
    private Map<String, String[]> list;
    private String[]              excludePaths = { "/api/**" };
    
    public Map<String, String[]> getList() {
        return list;
    }
    
    public void setList(Map<String, String[]> list) {
        this.list = list;
    }
    
    public String[] getExcludePaths() {
        return excludePaths;
    }
    
    public void setExcludePaths(String[] excludePaths) {
        this.excludePaths = excludePaths;
    }
}
