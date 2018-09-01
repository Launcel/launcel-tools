package xyz.launcel.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
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
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import xyz.launcel.aspejct.DataSourceSwitchAspect;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.bean.context.BeanDefinitionRegistryTool;
import xyz.launcel.constant.SessionFactoryConstant;
import xyz.launcel.exception.SystemError;
import xyz.launcel.holder.DataSourceConfigMap;
import xyz.launcel.holder.DynamicDataSource;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.interceptor.ParamInterceptor;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.properties.DataSourceProperties;
import xyz.launcel.properties.MybatisProperties;
import xyz.launcel.properties.RoleDataSourceHolder;

import javax.inject.Named;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by launcel on 2018/8/20.
 */
@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
public class DynamicSessionFactoryAutoConfiguration implements EnvironmentAware, BeanDefinitionRegistryPostProcessor
{
    private List<DataSourceConfigMap> dataSourceConfigMapList = new ArrayList<>();
    private MybatisProperties.MybatisPropertie mybatisPropertie;
    private boolean isDebugSql = false;

    @Override
    public void setEnvironment(Environment environment)
    {
        Binder binder = Binder.get(environment);
        binderDataSource(binder);
        binderMybatisConfig(binder);
    }

    private void binderDataSource(Binder binder)
    {
        DataSourceProperties multipleDataSource = binder.bind(SessionFactoryConstant.dataSourceConfigPrefix, Bindable.of(DataSourceProperties.class)).get();
        if (Objects.nonNull(multipleDataSource.getMain()))
        {
            DataSourceProperties.DataSourcePropertie main       = multipleDataSource.getMain();
            HikariDataSource                         dataSource = new HikariDataSource(main.getHikariConfig());
            dataSourceConfigMapList.add(new DataSourceConfigMap(main.getName(), main.getEnableTransactal(), main.getRoleDataSource(), dataSource));
            if (main.getRoleDataSource())
            {
                RoleDataSourceHolder.setDataSource(dataSource);
            }
            isDebugSql(multipleDataSource.getMain());
        }
        if (CollectionUtils.isNotEmpty(multipleDataSource.getOthers()))
        {
            multipleDataSource.getOthers().forEach(other ->
            {
                isDebugSql(other);
                HikariDataSource dataSource = new HikariDataSource(other.getHikariConfig());
                dataSourceConfigMapList.add(new DataSourceConfigMap(other.getName(), other.getEnableTransactal(), other.getRoleDataSource(), dataSource));
                RoleDataSourceHolder.setDataSource(dataSource);
            });
        }
    }

    private void binderMybatisConfig(Binder binder)
    {
        MybatisProperties mybatisProperties = binder.bind(SessionFactoryConstant.mybatisConfigPrefix, Bindable.of(MybatisProperties.class)).get();
        if (Objects.isNull(mybatisProperties.getMain()))
        {
            System.exit(-1);
        }
        mybatisPropertie = mybatisProperties.getMain();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource multipleDataSource()
    {
        DynamicDataSource   dynamicDataSources = new DynamicDataSource();
        Map<Object, Object> targetDataSources  = new HashMap<>();
        if (CollectionUtils.isEmpty(dataSourceConfigMapList))
        {
            throw new SystemError("_DEFINE_ERROR_CODE_010", ">>>  datasource propertie config or mybatis propertie config is null !!");
        }
        dataSourceConfigMapList.forEach(dataSourceConfigMap -> targetDataSources.put(dataSourceConfigMap.getName(), dataSourceConfigMap.getDataSource()));
        dynamicDataSources.setTargetDataSources(targetDataSources);
        dynamicDataSources.setDefaultTargetDataSource(dataSourceConfigMapList.get(0));
        return dynamicDataSources;
    }

    @Bean(name = "sqlSessionFactory")
    @DependsOn(value = "dataSource")
    public SqlSessionFactory sqlSessionFactory(@Named(value = "dataSource") DataSource dataSource) throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setConfigLocation(resourceLoader.getResource("classpath:mybatis/mybatis-config.xml"));
        sqlSessionFactory.setTypeAliasesPackage(mybatisPropertie.getAliasesPackage());
        sqlSessionFactory.setMapperLocations(resourceLoader.getResources(mybatisPropertie.getMapperResource()));
        List<Interceptor> interceptors = new ArrayList<>(2);
        interceptors.add(new PageInterceptor());
        if (isDebugSql) { interceptors.add(new ParamInterceptor()); }
        sqlSessionFactory.setPlugins((Interceptor[]) interceptors.toArray());
        return sqlSessionFactory.getObject();
    }

    @Bean
    @DependsOn(value = "sqlSessionFactory")
    public MapperScannerConfigurer registMapperScannerConfigurer()
    {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setBasePackage(mybatisPropertie.getMapperPackage());
        return configurer;
    }

    private void isDebugSql(DataSourceProperties.DataSourcePropertie dataSourcePropertie)
    {
        if (!isDebugSql && dataSourcePropertie.getDebugSql())
        {
            isDebugSql = true;
            return;
        }
        isDebugSql = false;
    }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.serviceaAspejctPrefix, value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public ServerAspejct serverAspejct() { return new ServerAspejct(); }

    @ConditionalOnProperty(prefix = SessionFactoryConstant.dataSourceConfigPrefix, value = "use-dynamic-data-source", havingValue = "true")
    @Bean
    public DataSourceSwitchAspect dataSourceSwitchAspect() { return new DataSourceSwitchAspect(); }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException
    {
        dataSourceConfigMapList.forEach(dataSourceConfigMap ->
        {
            if (dataSourceConfigMap.getEnableTransactal())
            {
                registTransactal(dataSourceConfigMap.getName(), registry, dataSourceConfigMap.getDataSource());
            }
        });
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

}
