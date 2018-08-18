package xyz.launcel.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.bean.context.BeanDefinitionRegistryTool;
import xyz.launcel.constant.SessionFactoryConstant;
import xyz.launcel.exception.SystemError;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.json.Json;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.log.RootLogger;
import xyz.launcel.properties.DataSourceProperties;
import xyz.launcel.properties.DataSourceProperties.DataSourcePropertie;
import xyz.launcel.properties.MybatisProperties;
import xyz.launcel.properties.MybatisProperties.MybatisPropertie;
import xyz.launcel.properties.RoleDataSourceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class MultipleSessionFactoryAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware
{
    private DataSourceProperties          multipleDataSource;
    private Map<String, MybatisPropertie> multipleMybatis;


    @Override
    public void setEnvironment(Environment environment)
    {
        Binder binder = Binder.get(environment);
        binderDataSource(binder);
        binderMybatisConfig(binder);
    }

    private void binderDataSource(Binder binder)
    {
        DataSourceProperties dataSourceProperties = binder.bind(SessionFactoryConstant.dataSourceConfigPrefix, Bindable.of(DataSourceProperties.class)).get();
    }

    private void binderMybatisConfig(Binder binder)
    {
        MybatisProperties mybatisProperties = binder.bind(SessionFactoryConstant.mybatisConfigPrefix, Bindable.of(MybatisProperties.class)).get();
        if (CollectionUtils.isEmpty(multipleMybatis))
        {
            multipleMybatis = new HashMap<>();
        }
        multipleMybatis.put(mybatisProperties.getMain().getRefName(), mybatisProperties.getMain());
        if (CollectionUtils.isNotEmpty(mybatisProperties.getOthers()))
        {
            mybatisProperties.getOthers().forEach(mybatisPropertie -> multipleMybatis.put(mybatisPropertie.getRefName(), mybatisPropertie));
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        if (Objects.nonNull(multipleDataSource) && CollectionUtils.isNotEmpty(multipleMybatis))
        {
            RootLogger.info("multipleDataSource is : {}\nmultipleMybatis is : {}", Json.toJson(multipleDataSource), Json.toJson(multipleMybatis));
            registBeans(multipleDataSource.getMain(), registry);
            if (CollectionUtils.isNotEmpty(multipleDataSource.getOthers()))
            {
                multipleDataSource.getOthers().forEach(propertie -> registBeans(propertie, registry));
            }
        }
        else
        {
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  datasource propertie config or mybatis propertie config is null !!");
        }
        RootLogger.warn("SessionFactory registry success");
    }


    private void registBeans(DataSourcePropertie dataSourcePropertie, BeanDefinitionRegistry registry)
    {
        MybatisPropertie mybatisPropertie          = multipleMybatis.get(dataSourcePropertie.getName());
        String           sqlSessionFactoryBeanName = dataSourcePropertie.getName() + SessionFactoryConstant.sessionFactoryName;
        HikariDataSource dataSource                = new HikariDataSource(dataSourcePropertie.getHikariConfig());


        registSessionFactory(registry, dataSource, mybatisPropertie, sqlSessionFactoryBeanName, dataSourcePropertie.getDebugSql());
        registMapperScannerConfigurer(registry, mybatisPropertie, sqlSessionFactoryBeanName);

        if (dataSourcePropertie.getEnableTransactal())
        {
            registTransactal(dataSourcePropertie.getName(), registry, dataSource);
        }
        if (dataSourcePropertie.getRoleDataSource())
        {
            registDataSource(registry, dataSource);
        }
    }

    /**
     * 注册 sessionFactory
     */
    private void registSessionFactory(BeanDefinitionRegistry registry, HikariDataSource hikariDataSource, MybatisPropertie mybatisPropertie, String sqlSessionFactoryBeanName, boolean isDebugSql)
    {
        AnnotatedGenericBeanDefinition sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        MutablePropertyValues          sqlSession    = sqlSessionAbd.getPropertyValues();

        sqlSession.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        sqlSession.addPropertyValue(SessionFactoryConstant.configLocationName, "classpath:mybatis/mybatis-config.xml");
        sqlSession.addPropertyValue(SessionFactoryConstant.typeAliasesPackageName, mybatisPropertie.getAliasesPackage());

        List<Interceptor> interceptors = new ArrayList<>(2);
        interceptors.add(new PageInterceptor());
        if (isDebugSql) { interceptors.add(new PageInterceptor()); }

        sqlSession.addPropertyValue(SessionFactoryConstant.pluginName, interceptors);
        try
        {
            sqlSession.addPropertyValue(SessionFactoryConstant.mapperLocationName, new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        }
        catch (IOException e)
        {
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  connot load resource :" + mybatisPropertie.getMapperResource() + " !!");
        }
        BeanDefinitionRegistryTool.registryBean(sqlSessionFactoryBeanName, registry, sqlSessionAbd);
    }

    /**
     * 注册当前 dataSource 以便其他程序中使用该 dataSource
     */
    private void registDataSource(BeanDefinitionRegistry registry, HikariDataSource hikariDataSource)
    {
        AnnotatedGenericBeanDefinition roleDataSourceAbd = BeanDefinitionRegistryTool.decorateAbd(RoleDataSourceHolder.class);
        MutablePropertyValues          roleDataSource    = roleDataSourceAbd.getPropertyValues();

        roleDataSource.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);

        BeanDefinitionRegistryTool.registryBean(SessionFactoryConstant.roleDateSourceName, registry, roleDataSourceAbd);
    }

    /**
     * 注册 MapperScannerConfigurer
     */
    private void registMapperScannerConfigurer(BeanDefinitionRegistry registry, MybatisPropertie mybatisPropertie, String sqlSessionFactoryBeanName)
    {
        AnnotatedGenericBeanDefinition abd                     = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
        MutablePropertyValues          mapperScannerConfigurer = abd.getPropertyValues();

        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.sqlSessionFactoryName, sqlSessionFactoryBeanName);
        mapperScannerConfigurer.addPropertyValue(SessionFactoryConstant.basePackageName, mybatisPropertie.getMapperPackage());
        String mybatisBeanName = mybatisPropertie.getRefName() + SessionFactoryConstant.mybatisName;

        BeanDefinitionRegistryTool.registryBean(mybatisBeanName, registry, abd);
    }

    /**
     * 为当前 dataSource 注册事物
     */
    private void registTransactal(String key, BeanDefinitionRegistry registry, HikariDataSource hikariDataSource)
    {
        AnnotatedGenericBeanDefinition transactalAbd            = BeanDefinitionRegistryTool.decorateAbd(DataSourceTransactionManager.class);
        MutablePropertyValues          transactaDataSourceValue = transactalAbd.getPropertyValues();

        transactaDataSourceValue.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        transactaDataSourceValue.addPropertyValue("enforceReadOnly", false);

        BeanDefinitionRegistryTool.registryBean(key + SessionFactoryConstant.transactionManagerName, registry, transactalAbd);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException { }

    @ConditionalOnProperty(prefix = "service.aspejct", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public ServerAspejct serverAspejct() { return new ServerAspejct(); }

    /**
     * 基于mapper代理则不需要注入
     *
     * @return
     */
    //    @Bean
    //    public SqlSessionTemplate sqlSessionTemplate(@Named(value = "sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    //        return new SqlSessionTemplate(sqlSessionFactory);
    //    }
}
