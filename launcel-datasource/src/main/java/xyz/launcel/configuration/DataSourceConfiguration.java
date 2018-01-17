package xyz.launcel.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.interceptor.PageInterceptor;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by Launcel in 2017/9/19
 */
@Configuration
//@EnableTransactionManagement
@EnableConfigurationProperties(value = DataSourceConfig.class)
public class DataSourceConfiguration {

    @Inject
    private DataSourceConfig config;

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;
    @Value("${mybatis.configLocation}")
    private String configLocation;
    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new HikariDataSource(config.getHikariConfig());
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        } catch (IOException x) {
            ExceptionFactory.error("_DEFINE_ERROR_CODE_002");
            System.exit(-1);
        }
        return sqlSessionFactoryBean;
    }

//    @Primary
//    @Bean
//    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier(value = "dataSource") DataSource dataSource) {
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
