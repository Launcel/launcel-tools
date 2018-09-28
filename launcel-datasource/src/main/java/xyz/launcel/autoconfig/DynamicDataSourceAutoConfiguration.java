package xyz.launcel.autoconfig;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.NonNull;
import xyz.launcel.aspejct.DataSourceSwitchAspect;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.constant.SessionFactoryConstant;
import xyz.launcel.exception.SystemError;
import xyz.launcel.holder.DataSourcePropertiesBinderTool;
import xyz.launcel.holder.DynamicDataSource;
import xyz.launcel.holder.MultipleDataSourceRegistryTool;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.interceptor.ParamInterceptor;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.log.RootLogger;

import javax.inject.Named;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by launcel on 2018/9/1.
 */
@Configuration
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class DynamicDataSourceAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware
{
    private DataSourcePropertiesBinderTool binderFacory;

    @Override
    public void setEnvironment(@NonNull Environment environment)
    {
        Binder binder = Binder.get(environment);
        binderFacory = new DataSourcePropertiesBinderTool();
        binderFacory.binderDataSource(binder);
        binderFacory.binderMybatisConfig(binder);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException
    {
        if (binderFacory.getDataSourceProperties().getUseDynamicDataSource())
        {
            binderFacory.getDynamicDataSourceConfigMapList()
                    .forEach(dataSourceConfigMap -> MultipleDataSourceRegistryTool.registTransactal(dataSourceConfigMap.getName(), registry,
                            dataSourceConfigMap.getDataSource()));
            return;
        }
        new MultipleDataSourceRegistryTool(binderFacory.getMultipleMybatis(), binderFacory.getDataSourceProperties()).registrMultipleBean(
                registry);
        RootLogger.warn("SessionFactory registry success");
    }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
    @Bean(name = "dataSource")
    @Primary
    public DataSource multipleDataSource()
    {
        DynamicDataSource   dynamicDataSources = new DynamicDataSource();
        Map<Object, Object> targetDataSources  = new HashMap<>();
        if (CollectionUtils.isEmpty(binderFacory.getDynamicDataSourceConfigMapList()))
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  datasource propertie config or mybatis propertie config is null !!");
        binderFacory.getDynamicDataSourceConfigMapList()
                .forEach(dataSourceConfigMap -> targetDataSources.put(dataSourceConfigMap.getName(), dataSourceConfigMap.getDataSource()));
        dynamicDataSources.setTargetDataSources(targetDataSources);
        dynamicDataSources.setDefaultTargetDataSource(binderFacory.getDynamicDataSourceConfigMapList().get(0));
        return dynamicDataSources;
    }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
    @Bean(name = "sqlSessionFactory")
    @DependsOn(value = "dataSource")
    public SqlSessionFactory sqlSessionFactory(@Named(value = "dataSource") DataSource dataSource) throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setConfigLocation(resourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
        sqlSessionFactory.setTypeAliasesPackage(binderFacory.getDynamicMybatisPropertie().getAliasesPackage());
        sqlSessionFactory.setMapperLocations(resourceLoader.getResources(binderFacory.getDynamicMybatisPropertie().getMapperResource()));
        List<Interceptor> interceptors = new ArrayList<>(2);
        interceptors.add(new PageInterceptor());
        if (binderFacory.debugSql())
        { interceptors.add(new ParamInterceptor()); }
        sqlSessionFactory.setPlugins((Interceptor[]) interceptors.toArray());
        return sqlSessionFactory.getObject();
    }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
    @Bean
    @DependsOn(value = "sqlSessionFactory")
    public MapperScannerConfigurer mapperScannerConfigurer()
    {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setBasePackage(binderFacory.getDynamicMybatisPropertie().getMapperPackage());
        return configurer;
    }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.serviceaAspejctPrefix, value = "enabled", havingValue = "true",
            matchIfMissing = true)
    @Bean
    public ServerAspejct serverAspejct() { return new ServerAspejct(); }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
    @Bean
    public DataSourceSwitchAspect dataSourceSwitchAspect() { return new DataSourceSwitchAspect(); }


    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException
    { }


}
