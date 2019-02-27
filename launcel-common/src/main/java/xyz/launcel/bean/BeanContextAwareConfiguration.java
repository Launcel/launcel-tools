package xyz.launcel.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import xyz.launcel.autoconfig.ThreadPoolAutoConfiguration;
import xyz.launcel.exception.ExceptionHelp;

@Configuration
@AutoConfigureAfter(value = ThreadPoolAutoConfiguration.class)
public class BeanContextAwareConfiguration implements ApplicationContextAware, InitializingBean
{
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        SpringBeanUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet()
    {
        ExceptionHelp.initProperties();
    }
}
