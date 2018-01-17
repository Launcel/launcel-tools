package xyz.launcel.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.validation.BindingResult;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.lang.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.CustomDataSourceProperties;
import xyz.launcel.prop.CustomMybatisProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = {CustomDataSourceProperties.class, CustomMybatisProperties.class})
public class CustomSessionFactoryConfiguration extends BaseLogger implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Map<String, CustomDataSourceProperties.CustomHikariDataSource> customDataSources = new HashMap<>();

    private Map<String, CustomMybatisProperties.CustomMybatisPropertie> customMybatis = new HashMap<>();


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        info("---------------------------------------------\n\tCustomHikariDataSource : {}",
                Json.toJson(customDataSources) + "\n---------------------------------------------");
        info("---------------------------------------------\n\tCustomMybatisPropertie : {}",
                Json.toJson(customMybatis) + "\n---------------------------------------------");
        customDataSources.keySet().stream().filter(StringUtils::isNotBlank).forEach(name -> {
//            registryBean(name + "DataSource", registry, CustomDataSourceProperties.CustomHikariDataSource.class);
            registryBean(name + "SqlSessionFactory", registry, SqlSessionFactoryBean.class);
            registryBean(name + "MapperSacnner", registry, MapperScannerConfigurer.class);
        });
//        customMybatis.keySet().stream().filter(StringUtils::isNotBlank).forEach(name ->
//                registryBean(name + "Mybatis", registry, CustomMybatisProperties.CustomMybatisPropertie.class));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!customDataSources.isEmpty() && !customMybatis.isEmpty()) {
            customDataSources.forEach((key, value) -> {
                CustomMybatisProperties.CustomMybatisPropertie temp = customMybatis.get(key + "Mybatis");
                MutablePropertyValues sqlSession = beanFactory.getBeanDefinition(key + "SqlSessionFactory").getPropertyValues();
                sqlSession.addPropertyValue("dataSource", new HikariDataSource(value.getHikariConfig()));
                sqlSession.addPropertyValue("configLocation", temp.getConfig());
                sqlSession.addPropertyValue("typeAliasesPackage", temp.getAliasesPackage());
                sqlSession.addPropertyValue("plugins", new Interceptor[]{new PageInterceptor()});
                try {
                    sqlSession.addPropertyValue("mapperLocations", new PathMatchingResourcePatternResolver().getResources(temp.getMapperResource()));
                } catch (IOException x) {
                    ExceptionFactory.error("_DEFINE_ERROR_CODE_002");
                    System.exit(-1);
                }
                MutablePropertyValues mapperScannerConfigurer = beanFactory.getBeanDefinition(key + "MapperSacnner").getPropertyValues();
                mapperScannerConfigurer.addPropertyValue("sqlSessionFactoryBeanName", key + "SqlSessionFactory");
                mapperScannerConfigurer.addPropertyValue("basePackage", temp.getMapperPackage());
            });
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        Map<String, Object> customDate = PropertySourceUtils.getSubProperties(env.getPropertySources(), "db.custom.jdbc");
        Map<String, Object> customMapper = PropertySourceUtils.getSubProperties(env.getPropertySources(), "db.custom.mybatis");
        dataBinderDataSource(customDate);
        dataBinderMapper(customMapper);
    }

    private void dataBinderDataSource(Map<String, Object> map) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(CustomDataSourceProperties.class);
        CustomDataSourceProperties customDataSourceProperties = new CustomDataSourceProperties();
        dataBinder(dataBinder, map);
        if (customDataSourceProperties.getList() != null) {
            for (CustomDataSourceProperties.CustomHikariDataSource hikariDataSource : customDataSourceProperties.getList().values()) {
                customDataSources.put(hikariDataSource.getName(), hikariDataSource);
            }
        }
    }

    private void dataBinderMapper(Map<String, Object> map) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(CustomMybatisProperties.class);
        CustomMybatisProperties customMybatisProperties = new CustomMybatisProperties();
        dataBinder(dataBinder, map);
        if (customMybatisProperties.getList() != null) {
            for (CustomMybatisProperties.CustomMybatisPropertie customMybatisPropertie : customMybatisProperties.getList().values()) {
                customMybatis.put(customMybatisPropertie.getRefName(), customMybatisPropertie);
            }

        }
    }

    private void dataBinder(RelaxedDataBinder dataBinder, Map<String, Object> map) {
        dataBinder.bind(new MutablePropertyValues(map));
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            ExceptionFactory.error("-1", "多数据源绑定失败！");
            System.exit(-1);
        }
    }

    /**
     * 注入 custom datasource bean
     *
     * @param dataBeanName
     * @param registry
     */
    private void registryBean(String dataBeanName, BeanDefinitionRegistry registry, Class<?> clazz) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
        ScopeMetadata scopeMetadata = new AnnotationScopeMetadataResolver().resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        String beanName = (dataBeanName != null ? dataBeanName : scopeMetadata.getScopeName());
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }
}
