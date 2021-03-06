package xyz.launcel.bean.context;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import xyz.launcel.exception.ExceptionHelp;

/**
 * Created by launcel on 2018/8/18.
 */
public class BeanSourceContextAware implements ApplicationContextAware, InitializingBean
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
