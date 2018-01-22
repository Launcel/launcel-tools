package xyz.launcel.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.aspejct.ServerAspejct;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.prop.DataSourceProperties;
import xyz.launcel.prop.MybatisProperties;

import java.io.IOException;

/**
 * Created by Launcel in 2017/9/19
 */
@Configuration
@EnableConfigurationProperties(value = {DataSourceProperties.class, MybatisProperties.class})
//@EnableTransactionManagement
public class SessionFactoryAutoConfiguration {

    private final DataSourceProperties dataSourceProperties;

    private final MybatisProperties mybatisProperties;

    public SessionFactoryAutoConfiguration(DataSourceProperties dataSourceProperties, MybatisProperties mybatisProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.mybatisProperties = mybatisProperties;
    }

    @Primary
    @Bean(name = "dataSource")
    public HikariDataSource dataSource() {
        return new HikariDataSource(dataSourceProperties.getHikariConfig());
    }

    @ConditionalOnBean(name = "dataSource")
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") HikariDataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getAliasesPackage());
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisProperties.getMapperResource()));
        } catch (IOException x) {
            ExceptionFactory.error("-1", "xml文件加载失败");
            System.exit(-1);
        }
//        new MapperScannerRegistrar(sqlSessionFactoryBean.getObject(), mybatisProperties.getMapperPackage()).registryBean(registry);
        return sqlSessionFactoryBean;
    }

//    @ConditionalOnBean(name = "sqlSessionFactory")
//    @Bean
//    String registryMapper() {
//        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
//        AnnotatedGenericBeanDefinition abd = BeanDefinitionRegistryTool.decorateAbd(MapperScannerConfigurer.class);
//        MutablePropertyValues mapperScannerConfigurer = abd.getPropertyValues();
//        mapperScannerConfigurer.addPropertyValue("sqlSessionFactoryBeanName", "sqlSessionFactory");
//        mapperScannerConfigurer.addPropertyValue("basePackage", mybatisProperties.getMapperPackage());
//        BeanDefinitionRegistryTool.registryBean("mapperScannerConfigurer", registry, abd);
//        return "init-mapper-scanner";
//    }

    @ConditionalOnProperty(prefix = "aspejct.service", value = "enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public ServerAspejct serverAspejct() {
        return new ServerAspejct();
    }


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
