package xyz.launcel.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.launcel.prop.DozerProperties;

import javax.inject.Inject;

@ConditionalOnProperty(prefix = "launcel.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerProperties.class)
public class DozerConfiguration {

    @Inject
    private DozerProperties dozerProperties;

    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (dozerProperties.getList() != null && !dozerProperties.getList().isEmpty()) {
            mapper.setMappingFiles(dozerProperties.getList());
        }
        return mapper;
    }
}
