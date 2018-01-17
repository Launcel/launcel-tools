package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "launcel.dozer")
public class DozerProperties {

    @SuppressWarnings("unchecked")
    private List<String> list = Collections.EMPTY_LIST;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
