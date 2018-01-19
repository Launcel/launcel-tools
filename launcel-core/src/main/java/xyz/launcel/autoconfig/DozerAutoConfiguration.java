package xyz.launcel.autoconfig;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.DozerProperties;

import javax.inject.Inject;

@ConditionalOnProperty(prefix = "common.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerProperties.class)
public class DozerAutoConfiguration extends BaseLogger {

    @Inject
    private DozerProperties dozerProperties;

    @Lazy
    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (dozerProperties.getList() != null && !dozerProperties.getList().isEmpty()) {
            mapper.setMappingFiles(dozerProperties.getList());
        }
        info(">>>  dozer mapper list is null!");
        return mapper;
    }
}
