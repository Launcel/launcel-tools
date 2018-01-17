package xyz.launcel.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import xyz.launcel.prop.DozerMapperProp;

import javax.inject.Inject;

@ConditionalOnProperty(prefix = "launcel.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerMapperProp.class)
public class DozerConfiguration {

    @Inject
    private DozerMapperProp dozerMapperProp;

    @Lazy
    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (!dozerMapperProp.getList().isEmpty())
            mapper.setMappingFiles(dozerMapperProp.getList());
        return mapper;
    }
}
