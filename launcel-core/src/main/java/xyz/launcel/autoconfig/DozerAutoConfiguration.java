package xyz.launcel.autoconfig;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import xyz.launcel.lang.Json;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.DozerProperties;

@ConditionalOnProperty(prefix = "common.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerProperties.class)
public class DozerAutoConfiguration extends BaseLogger {

    private final DozerProperties dozerProperties;

    public DozerAutoConfiguration(DozerProperties dozerProperties) {
        this.dozerProperties = dozerProperties;
    }

    @Lazy
    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (CollectionUtils.isEmpty(dozerProperties.getList())) {
            if (isDebugEnabled())
                debug(">>>  dozer mapper list is : " + Json.toJson(dozerProperties.getList()));
            return mapper;
        }
        mapper.setMappingFiles(dozerProperties.getList());
        return mapper;
    }


}
