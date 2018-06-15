package xyz.launcel.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.validation.BindingResult;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.BeanDefinitionRegistryTool;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.DataSourceProperties;
import xyz.launcel.prop.DataSourceProperties.DataSourcePropertie;
import xyz.launcel.prop.MybatisProperties;
import xyz.launcel.prop.MybatisProperties.MybatisPropertie;
import xyz.launcel.prop.RoleDataSourceHolder;
import xyz.launcel.constant.SessionFactoryConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties(value = {DataSourceProperties.class, MybatisProperties.class})
public class MultipleSessionFactoryAutoConfiguration extends BaseLogger implements BeanDefinitionRegistryPostProcessor, EnvironmentAware
{

    private          List<DataSourcePropertie>     multipleDataSource    = new ArrayList<>();
    private          Map<String, MybatisPropertie> multipleMybatis       = new HashMap<>();
    private volatile Boolean                       isFirstRoleDataSource = true;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        if (!multipleDataSource.isEmpty() && !multipleMybatis.isEmpty())
        {
//            INFO("multipleDataSource is : {}\nmultipleMybatis is : {}", Json.toJson(multipleDataSource), Json.toJson(multipleMybatis));
            multipleDataSource.forEach(propertie -> registBeans(propertie, registry));
        }
        else
        {
            ExceptionFactory.error(">>>  datasource propertie config or mybatis propertie config is null !!");
            System.exit(-1);
        }
//        RootLogger.WARN("SessionFactory registry success");
    }

    private void registBeans(DataSourcePropertie dataSourcePropertie, BeanDefinitionRegistry registry)
    {
        MybatisPropertie mybatisPropertie = multipleMybatis.get(dataSourcePropertie.getName());

        if (Objects.isNull(mybatisPropertie))
        {
            ExceptionFactory.error(">>>  mybatis propertie config not find ref-name :" + dataSourcePropertie.getName() + " !!");
            System.exit(-1);
        }

        String           sqlSessionFactoryBeanName = dataSourcePropertie.getName() + SessionFactoryConstant.sessionFactoryName;
        HikariDataSource dataSource                = new HikariDataSource(dataSourcePropertie.getHikariConfig());


        registSessionFactory(registry, dataSource, mybatisPropertie, sqlSessionFactoryBeanName);
        registMapperScannerConfigurer(registry, mybatisPropertie, sqlSessionFactoryBeanName);

        if (dataSourcePropertie.getEnableTransactal())
        {
            registTransactal(dataSourcePropertie.getName(), registry, dataSource);
        }
        if (dataSourcePropertie.getRoleDataSource() && isFirstRoleDataSource)
        {
            registDataSource(registry, dataSource);
            isFirstRoleDataSource = false;
        }
    }

    /**
     * 注册 sessionFactory
     */
    private void registSessionFactory(BeanDefinitionRegistry registry, HikariDataSource hikariDataSource, MybatisPropertie mybatisPropertie, String sqlSessionFactoryBeanName)
    {
        AnnotatedGenericBeanDefinition sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        MutablePropertyValues          sqlSession    = sqlSessionAbd.getPropertyValues();

        sqlSession.addPropertyValue(SessionFactoryConstant.dataSourceName, hikariDataSource);
        sqlSession.addPropertyValue(SessionFactoryConstant.configLocationName, "classpath:mybatis/mybatis-config.xml");
        sqlSession.addPropertyValue(SessionFactoryConstant.typeAliasesPackageName, mybatisPropertie.getAliasesPackage());

        sqlSession.addPropertyValue(SessionFactoryConstant.pluginName, Collections.singletonList(new PageInterceptor()));
        try
        {
            sqlSession.addPropertyValue(SessionFactoryConstant.mapperLocationName, new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        }
        catch (IOException e)
        {
            ExceptionFactory.error(">>>  connot load resource :" + mybatisPropertie.getMapperResource() + " !!");
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

    @Override
    public void setEnvironment(Environment environment)
    {
        ConfigurableEnvironment env                   = (ConfigurableEnvironment) environment;
        Map<String, Object>     multipleDataSourceMap = PropertySourceUtils.getSubProperties(env.getPropertySources(), SessionFactoryConstant.dataSourceConfigPrefix);
        Map<String, Object>     multipleMybatisMap    = PropertySourceUtils.getSubProperties(env.getPropertySources(), SessionFactoryConstant.mybatisConfigPrefix);
        dataBinderDataSource(multipleDataSourceMap);
        dataBinderMapper(multipleMybatisMap);
    }

    private void dataBinderDataSource(Map<String, Object> map)
    {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        RelaxedDataBinder    dataBinder           = new RelaxedDataBinder(dataSourceProperties);
        // 设置数据源属性
        dataBinder(dataBinder, map);
        multipleDataSource.add(dataSourceProperties.getMain());
        if (dataSourceProperties.getOthers() != null) { multipleDataSource.addAll(dataSourceProperties.getOthers()); }
    }

    private void dataBinderMapper(Map<String, Object> map)
    {
        MybatisProperties mybatisProperties = new MybatisProperties();
        RelaxedDataBinder dataBinder        = new RelaxedDataBinder(mybatisProperties);
        dataBinder(dataBinder, map);
        multipleMybatis.put(mybatisProperties.getMain().getRefName(), mybatisProperties.getMain());
        if (mybatisProperties.getOthers() != null)
        {
            mybatisProperties.getOthers().forEach(mybatisPropertie -> multipleMybatis.put(mybatisPropertie.getRefName(), mybatisPropertie));
        }
    }

    private void dataBinder(RelaxedDataBinder dataBinder, Map<String, Object> map)
    {
        dataBinder.bind(new MutablePropertyValues(map));
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors())
        {
            ExceptionFactory.error("多数据源绑定失败！");
            System.exit(-1);
        }
    }

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
