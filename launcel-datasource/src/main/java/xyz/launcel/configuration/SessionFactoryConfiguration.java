package xyz.launcel.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.PageInterceptor;
import xyz.launcel.lang.Json;
import xyz.launcel.prop.DataSourceProperties;
import xyz.launcel.prop.MybatisProperties;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by Launcel in 2017/9/19
 */
@Configuration
//@EnableTransactionManagement
@EnableConfigurationProperties(value = {DataSourceProperties.class, MybatisProperties.class})
public class SessionFactoryConfiguration {

    @Inject
    private DataSourceProperties config;

    @Inject
    private MybatisProperties mybatisProperties;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new HikariDataSource(config.getHikariConfig());
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    @ConditionalOnBean(name = "dataSource")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisProperties.getConfig()));
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getAliasesPackage());
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisProperties.getMapperResource()));
        } catch (IOException x) {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_002");
            System.exit(-1);
        }
        return sqlSessionFactoryBean;
    }

    @ConditionalOnBean(name = {"sqlSessionFactory", "mybatisProperties"})
    @Bean
    MapperScannerConfigurer mapperScannerConfigurer() {
        System.out.println("\n----------------------------"+ Json.toJson(mybatisProperties.getMapperResource()));
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisProperties.getMapperPackage());
        return mapperScannerConfigurer;
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
