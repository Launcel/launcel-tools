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
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.validation.BindingResult;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.hook.BeanDefinitionRegistryTool;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.prop.CustomDataSourceProperties;
import xyz.launcel.prop.CustomDataSourceProperties.PrimyHikariDataSource;
import xyz.launcel.prop.CustomMybatisProperties;
import xyz.launcel.prop.CustomMybatisProperties.PrimyMybatisPropertie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = {CustomDataSourceProperties.class, CustomMybatisProperties.class})
public class CustomSessionFactoryAutoConfiguration extends BaseLogger implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private List<PrimyHikariDataSource> customDataSources = new ArrayList<>();

    private Map<String, PrimyMybatisPropertie> customMybatis = new HashMap<>();


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        addMybatisEvcironment(registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    private void registCusTomSqlSessionFactory(BeanDefinitionRegistry registry, PrimyMybatisPropertie mybatisPropertie, PrimyHikariDataSource dataSource) {
        AnnotatedGenericBeanDefinition sqlSessionAbd = BeanDefinitionRegistryTool.decorateAbd(SqlSessionFactoryBean.class);
        MutablePropertyValues sqlSession = sqlSessionAbd.getPropertyValues();
        sqlSession.addPropertyValue("dataSource", new HikariDataSource(dataSource.getHikariConfig()));
        sqlSession.addPropertyValue("configLocation", "classpath:mybatis/mybatis-config.xml");
        sqlSession.addPropertyValue("typeAliasesPackage", mybatisPropertie.getAliasesPackage());
        sqlSession.addPropertyValue("plugins", new Interceptor[]{new PageInterceptor()});
        try {
            sqlSession.addPropertyValue("mapperLocations", new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        } catch (IOException x) {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_002");
            System.exit(-1);
        }
        BeanDefinitionRegistryTool.registryBean(dataSource.getName() + "SqlSessionFactory", registry, sqlSessionAbd);
    }


    private void addMybatisEvcironment(BeanDefinitionRegistry registry) {
        if (!customDataSources.isEmpty() && !customMybatis.isEmpty()) {
            customDataSources.forEach(dataSource -> {
                PrimyMybatisPropertie temp = customMybatis.get(dataSource.getName());
                registCusTomSqlSessionFactory(registry, temp, dataSource);
                AnnotatedGenericBeanDefinition abd = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
                MutablePropertyValues mapperScannerConfigurer = abd.getPropertyValues();
                mapperScannerConfigurer.addPropertyValue("sqlSessionFactoryBeanName", dataSource.getName() + "SqlSessionFactory");
                mapperScannerConfigurer.addPropertyValue("basePackage", temp.getMapperPackage());
                BeanDefinitionRegistryTool.registryBean(dataSource.getName() + "MapperScannerConfigurer", registry, abd);
            });
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        Map<String, Object> customDate = PropertySourceUtils.getSubProperties(env.getPropertySources(), "custom.jdbc");
        Map<String, Object> customMapper = PropertySourceUtils.getSubProperties(env.getPropertySources(), "custom.mybatis");
        dataBinderDataSource(customDate);
        dataBinderMapper(customMapper);
    }

    private void dataBinderDataSource(Map<String, Object> map) {
        CustomDataSourceProperties customDataSourceProperties = new CustomDataSourceProperties();
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(customDataSourceProperties);
        // 设置数据源属性
        dataBinder(dataBinder, map);
        if (customDataSourceProperties.getList() != null) {
            customDataSources.addAll(customDataSourceProperties.getList());
        }
    }


    private void dataBinderMapper(Map<String, Object> map) {
        CustomMybatisProperties customMybatisProperties = new CustomMybatisProperties();
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(customMybatisProperties);
        dataBinder(dataBinder, map);
        if (customMybatisProperties.getList() != null)
            customMybatisProperties.getList().forEach(customMybatisPropertie ->
                    customMybatis.put(customMybatisPropertie.getRefName(), customMybatisPropertie));
    }

    private void dataBinder(RelaxedDataBinder dataBinder, Map<String, Object> map) {
        dataBinder.bind(new MutablePropertyValues(map));
        BindingResult bindingResult = dataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            ExceptionFactory.error("-1", "多数据源绑定失败！");
            System.exit(-1);
        }
    }
}
