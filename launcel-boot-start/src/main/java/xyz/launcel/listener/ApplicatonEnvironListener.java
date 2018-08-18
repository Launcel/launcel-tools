package xyz.launcel.listener;

import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by launcel on 2018/8/18.
 */
public class ApplicatonEnvironListener implements SpringApplicationRunListener, PriorityOrdered
{

    @Override
    public void starting() { }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment)
    {
        Properties properties = new Properties();
        try
        {
            //demo.properties就是我们自定义的配置文件，extension是自定义目录
            properties.load(this.getClass().getClassLoader().getResourceAsStream("launcel-application.yml"));
            PropertySource propertySource = new PropertiesPropertySource("launcel", properties);
            //PropertySource是资源加载的核心
            MutablePropertySources propertySources = environment.getPropertySources();
            //这里添加最后
            propertySources.addLast(propertySource);
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        return 5;
    }
}
