package xyz.launcel.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.hook.ApplicationContextHook;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CommonConfiguration {

    @Inject
    private ApplicationContext context;

    @Lazy
    @Bean(name = "dozer")
    @SuppressWarnings("unchecked")
    public Mapper mapper() {
        Environment env = context.getEnvironment();
        DozerBeanMapper mapper = new DozerBeanMapper();
        if (env.containsProperty("launcel.dozer.mapperList")) {
            String list = env.getProperty("launcel.dozer.mapperList");
            mapper.setMappingFiles((List<String>) StringUtils.spiltStream(list, ",").collect(Collectors.toList()));
        }
        return mapper;
    }

    @PostConstruct
    public void initHook() {
        ApplicationContextHook.setApplicationContext(context);
    }

}
