package xyz.launcel.autoconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
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
import xyz.launcel.lang.Json;
import xyz.launcel.prop.DataSourceProps;
import xyz.launcel.prop.MybatisProps;

import java.io.IOException;

/**
 * Created by Launcel in 2017/9/19
 */
@Configuration
@EnableConfigurationProperties(value = {DataSourceProps.class, MybatisProps.class})
//@EnableTransactionManagement
public class SessionFactoryAutoConfiguration {

    private final DataSourceProps dataSourceProps;

    private final MybatisProps mybatisProps;

    public SessionFactoryAutoConfiguration(DataSourceProps dataSourceProps, MybatisProps mybatisProps) {
        this.dataSourceProps = dataSourceProps;
        this.mybatisProps = mybatisProps;
    }

    @Primary
    @Bean(name = "dataSource")
    public HikariDataSource dataSource() {
        System.out.println("-------------------------");
        System.out.println(Json.toJson(mybatisProps));
        System.out.println(Json.toJson(dataSourceProps));
        System.out.println("-------------------------");
        return new HikariDataSource(dataSourceProps.getHikariConfig());
    }

    @ConditionalOnBean(name = "dataSource")
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier(value = "dataSource") HikariDataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProps.getAliasesPackage());
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mybatisProps.getMapperResource()));
        } catch (IOException x) {
            ExceptionFactory.error("-1", "xml文件加载失败");
            System.exit(-1);
        }
        return sqlSessionFactoryBean;
    }

    @ConditionalOnBean(name = "sqlSessionFactory")
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier(value = "sqlSessionFactory") SqlSessionFactory sqlSessionFactoryBean) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setSqlSessionFactory(sqlSessionFactoryBean);
        mapperScannerConfigurer.setBasePackage(mybatisProps.getMapperPackage());
        return mapperScannerConfigurer;
    }

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
