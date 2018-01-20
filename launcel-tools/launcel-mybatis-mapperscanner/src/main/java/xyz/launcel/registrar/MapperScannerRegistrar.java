/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.20
 * Version: 1.0
 */

package xyz.launcel.registrar;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.util.List;

public class MapperScannerRegistrar {
    private static ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

    private String sqlSessionFactoryBeanName;

    private List<String> basePackages;

    private SqlSessionFactory sqlSessionFactoryBean;

    public void registryBean(BeanDefinitionRegistry registry) {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        if (StringUtils.hasText(sqlSessionFactoryBeanName))
            scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        if (sqlSessionFactoryBean != null)
            scanner.setSqlSessionFactory(sqlSessionFactoryBean);
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }


    public MapperScannerRegistrar(String sqlSessionFactoryBeanName, List<String> basePackages) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
        this.basePackages = basePackages;
    }

    public MapperScannerRegistrar(SqlSessionFactory sqlSessionFactoryBean, List<String> basePackages) {
        this.basePackages = basePackages;
        this.sqlSessionFactoryBean = sqlSessionFactoryBean;
    }
}
