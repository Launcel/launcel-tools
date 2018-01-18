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
import xyz.launcel.prop.DataSourceProperties;
import xyz.launcel.prop.MybatisProperties;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by Launcel in 2017/9/19
 */
//@EnableTransactionManagement
@Configuration
@EnableConfigurationProperties({DataSourceProperties.class, MybatisProperties.class})
public class SessionFactoryConfiguration {


    @Inject
    private DataSourceProperties dataSourcePropertie;

    @Inject
    private MybatisProperties mybatisPropertie;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new HikariDataSource(dataSourcePropertie.getHikariConfig());
    }

    @ConditionalOnBean(name = "dataSource")
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        System.out.println("\n---------------------------------\t" + mybatisPropertie.getAliasesPackage() + "\n---------------------------------");
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisPropertie.getAliasesPackage());
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisPropertie.getMapperResource()));
        } catch (IOException x) {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_002");
            System.exit(-1);
        }
        return sqlSessionFactoryBean;
    }

    @ConditionalOnBean(name = "sqlSessionFactory")
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier(value = "sqlSessionFactory") SqlSessionFactoryBean sqlSessionFactory) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisPropertie.getMapperPackage());
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
