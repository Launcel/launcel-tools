package xyz.launcel.autoconfig;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.json.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.DozerProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnProperty(prefix = "common.dozer", value = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = DozerProperties.class)
public class DozerAutoConfiguration extends BaseLogger {
    
    private final DozerProperties properties;
    
    public DozerAutoConfiguration(DozerProperties dozerProperties) {
        this.properties = dozerProperties;
    }
    
    //    @Lazy
    @Bean(name = "dozer")
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (StringUtils.isNotBlank(properties.getPath())) {
            try {
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/" + properties.getPath());
                List<String> xmlString = Arrays.stream(resources).map(t -> {
                    try {
                        return t.getFile().toURI().toURL().toString();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                if (isDebug())
                    DEBUG(">>>  dozer mapper list is : " + Json.toJson(xmlString));
                mapper.setMappingFiles(xmlString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mapper;
    }
    
    
}
