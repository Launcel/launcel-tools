package xyz.launcel.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Properties;

/**
 * Created by launcel on 2018/8/31.
 */
public class LoggerEnvironListener implements SpringApplicationRunListener
{

    private final SpringApplication application;

    private final String[] args;

    public LoggerEnvironListener(SpringApplication application, String[] args)
    {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting()
    {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void started(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void running(ConfigurableApplicationContext context)
    {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception)
    {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        Properties properties = new Properties();
        String     path       = "classpath:logback-dev.xml";
        String     activity   = environment.getActiveProfiles()[0];
        if (activity.equals("prod"))
        {
            path = "classpath:logback-prod.xml";
            properties.put("logging.level.root", "error");
        }
        properties.put("logging.config", path);
        MutablePropertySources source  = environment.getPropertySources();
        PropertySource         propert = new PropertiesPropertySource("logging", properties);
        source.addFirst(propert);
    }
}
