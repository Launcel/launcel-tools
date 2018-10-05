package xyz.launcel.bean.context;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.json.builder.DefaultGsonBuilder;
import xyz.launcel.properties.JsonProperties;

/**
 * Created by launcel on 2018/8/18.
 */
@EnableConfigurationProperties(value = {JsonProperties.class})
@RequiredArgsConstructor
public class BeanSourceContextAware implements ApplicationContextAware, InitializingBean
{
    private final JsonProperties properties;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException
    {
        SpringBeanUtil.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet()
    {
        DefaultGsonBuilder.builder()
                .dateFormat(properties.getDateFormat())
                .floatingPointValues(properties.getFloatingPointValue())
                .formatPrint(properties.getFormatPrint())
                .serializeNull(properties.getSerializeNull())
                .version(properties.getVersion())
                .build();
        
        ExceptionHelp.initProperties();
    }
}
