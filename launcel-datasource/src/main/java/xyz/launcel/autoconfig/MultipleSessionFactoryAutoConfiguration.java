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
import org.springframework.validation.BindingResult;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.constant.SessionConstant;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.BeanDefinitionRegistryTool;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.DataSourceProperties;
import xyz.launcel.prop.DataSourceProperties.DataSourcePropertie;
import xyz.launcel.prop.MybatisProperties;
import xyz.launcel.prop.MybatisProperties.MybatisPropertie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = {DataSourceProperties.class, MybatisProperties.class})
public class MultipleSessionFactoryAutoConfiguration extends BaseLogger implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private List<DataSourcePropertie> multipleDataSource = new ArrayList<>();

    private Map<String, MybatisPropertie> multipleMybatis = new HashMap<>();

    @Override

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!multipleDataSource.isEmpty())
            multipleDataSource.forEach(dataSourcePropertie -> registSessionFactory(dataSourcePropertie, registry));
    }

    private void registSessionFactory(DataSourcePropertie dataSourcePropertie, BeanDefinitionRegistry registry) {
        // 注册 sessionFactory
        AnnotatedGenericBeanDefinition sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        MutablePropertyValues sqlSession = sqlSessionAbd.getPropertyValues();
        sqlSession.addPropertyValue(SessionConstant.dataSourceName, new HikariDataSource(dataSourcePropertie.getHikariConfig()));
        sqlSession.addPropertyValue(SessionConstant.configLocationName, SessionConstant.configLocationValue);
        MybatisPropertie mybatisPropertie = multipleMybatis.get(dataSourcePropertie.getName());
        sqlSession.addPropertyValue(SessionConstant.typeAliasesPackageName, mybatisPropertie.getAliasesPackage());
        sqlSession.addPropertyValue(SessionConstant.pluginName, new Interceptor[]{new PageInterceptor()});
        try {
            sqlSession.addPropertyValue(SessionConstant.mapperLocationName, new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        } catch (IOException e) {
            System.exit(-1);
        }
        String sqlSessionFactoryBeanName = dataSourcePropertie.getName() + SessionConstant.sessionFactoryName;
        BeanDefinitionRegistryTool.registryBean(sqlSessionFactoryBeanName, registry, sqlSessionAbd);
        // 注册 MapperScannerConfigurer
        AnnotatedGenericBeanDefinition abd = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
        MutablePropertyValues mapperScannerConfigurer = abd.getPropertyValues();
        mapperScannerConfigurer.addPropertyValue(SessionConstant.sqlSessionFactoryName, sqlSessionFactoryBeanName);
        mapperScannerConfigurer.addPropertyValue(SessionConstant.basePackageName, mybatisPropertie.getMapperPackage());
        String mybatisBeanName = dataSourcePropertie.getName() + SessionConstant.mybatisName;
        BeanDefinitionRegistryTool.registryBean(mybatisBeanName, registry, abd);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        Map<String, Object> multipleDataSourceMap = PropertySourceUtils.getSubProperties(env.getPropertySources(), SessionConstant.dataSourceConfigPrefix);
        Map<String, Object> multipleMybatisMap = PropertySourceUtils.getSubProperties(env.getPropertySources(), SessionConstant.mybatisConfigPrefix);
        dataBinderDataSource(multipleDataSourceMap);
        dataBinderMapper(multipleMybatisMap);
    }

    private void dataBinderDataSource(Map<String, Object> map) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSourceProperties);
        // 设置数据源属性
        dataBinder(dataBinder, map);
        multipleDataSource.add(dataSourceProperties.getMain());
        if (dataSourceProperties.getOthers() != null) {
            multipleDataSource.addAll(dataSourceProperties.getOthers());
        }
    }

    private void dataBinderMapper(Map<String, Object> map) {
        MybatisProperties mybatisProperties = new MybatisProperties();
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(mybatisProperties);
        dataBinder(dataBinder, map);
        multipleMybatis.put(mybatisProperties.getMain().getRefName(), mybatisProperties.getMain());
        if (mybatisProperties.getOthers() != null)
            mybatisProperties.getOthers().forEach(mybatisPropertie -> multipleMybatis.put(mybatisPropertie.getRefName(), mybatisPropertie));
    }

    private void dataBinder(RelaxedDataBinder dataBinder, Map<String, Object> map) {
        dataBinder.bind(new MutablePropertyValues(map));
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            ExceptionFactory.error("-1", "多数据源绑定失败！");
            System.exit(-1);
        }
    }

    @ConditionalOnProperty(prefix = "aspejct.service", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public ServerAspejct serverAspejct() {
        return new ServerAspejct();
    }

//    @ConditionalOnProperty(prefix = "db.transaction", value = "enabled", havingValue = "true")
//    @Bean
//    public PlatformTransactionManager prodTransactionManager(@Named(value = "dataSource") HikariDataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }


    /**
     * 基于mapper代理则不需要注入
     *
     * @return
     */
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(value = "sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
}
