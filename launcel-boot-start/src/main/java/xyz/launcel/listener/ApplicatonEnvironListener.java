package xyz.launcel.listener;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * Created by launcel on 2018/8/18.
 */
@RequiredArgsConstructor
public class ApplicatonEnvironListener implements SpringApplicationRunListener, PriorityOrdered
{
    private final SpringApplication application;
    private final String[]          args;

    @Override
    public void starting() { }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        var yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("launcel-application.yml"));
        if (!Objects.nonNull(yaml.getObject()))
        {
            var source  = environment.getPropertySources();
            var propert = new PropertiesPropertySource("launcel-application", yaml.getObject());
            source.addFirst(propert);
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) { }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) { }

    @Override
    public void started(ConfigurableApplicationContext context) {}

    @Override
    public void running(ConfigurableApplicationContext context) {}

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {}

    @Override
    public int getOrder()
    {
        return LOWEST_PRECEDENCE;
    }
}
