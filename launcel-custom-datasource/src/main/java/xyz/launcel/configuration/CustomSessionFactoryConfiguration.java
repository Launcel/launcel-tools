package xyz.launcel.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.CustomDataSourceProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties
public class CustomSessionFactoryConfiguration extends BaseLogger implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {


    Map<String, CustomDataSourceProperties.CustomHikariDataSource> customDataSources = new HashMap<>();


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        customDataSources.keySet().stream().filter(StringUtils::isNotBlank).forEach(name -> registryBean(name, registry));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        Map<String, Object> map = PropertySourceUtils.getSubProperties(env.getPropertySources(), "db.custom.jdbc");
        dataBinder(map);
    }

    private void dataBinder(Map<String, Object> map) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(CustomDataSourceProperties.class);
        CustomDataSourceProperties customDataSourceProperties = new CustomDataSourceProperties();
        dataBinder.bind(new MutablePropertyValues(map));
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            ExceptionFactory.error("-1", "多数据源绑定失败！");
            System.exit(-1);
        }
        if (!Objects.requireNonNull(customDataSourceProperties.getList()).isEmpty()) {
            customDataSources.putAll(customDataSourceProperties.getList());
        }
    }

    /**
     * 注入 custom datasource bean
     *
     * @param dataBeanName
     * @param registry
     */
    private void registryBean(String dataBeanName, BeanDefinitionRegistry registry) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(HikariDataSource.class);
        // 单例还是原型等等...作用域对象.
        ScopeMetadata scopeMetadata = new AnnotationScopeMetadataResolver().resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        // 可以自动生成name
        String beanName = (dataBeanName != null ? dataBeanName : scopeMetadata.getScopeName());
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }
}
