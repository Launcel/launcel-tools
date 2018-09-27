package xyz.launcel.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by launcel on 2018/8/18.
 */
@RequiredArgsConstructor
public class ApplicatonEnvironListener implements SpringApplicationRunListener, PriorityOrdered
{
    private final String[]          args;
    private final SpringApplication application;

    @Override
    public void starting() { }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("launcel-application.yml"));
        MutablePropertySources source  = environment.getPropertySources();
        PropertySource         propert = new PropertiesPropertySource("launcel-application", yaml.getObject());
        source.addFirst(propert);
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
