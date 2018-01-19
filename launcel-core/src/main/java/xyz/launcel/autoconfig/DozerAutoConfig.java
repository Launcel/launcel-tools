package xyz.launcel.autoconfig;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.launcel.prop.DozerPropertie;

import javax.inject.Inject;

@ConditionalOnProperty(prefix = "common.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerPropertie.class)
public class DozerAutoConfig {

    @Inject
    private DozerPropertie dozerPropertie;

    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (dozerPropertie.getList() != null && !dozerPropertie.getList().isEmpty()) {
            mapper.setMappingFiles(dozerPropertie.getList());
        }
        return mapper;
    }
}
