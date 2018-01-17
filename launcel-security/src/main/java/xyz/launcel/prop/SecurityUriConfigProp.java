package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "launcel.security")
public class SecurityUriConfigProp {
    private Map<String, String[]> list;

    public Map<String, String[]> getList() {
        return list;
    }

    public void setList(Map<String, String[]> list) {
        this.list = list;
    }
}
