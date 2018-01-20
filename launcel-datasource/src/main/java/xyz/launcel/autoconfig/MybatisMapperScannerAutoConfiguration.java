/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.19
 * Version: 1.0
 */

package xyz.launcel.autoconfig;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.launcel.prop.MybatisProperties;

@Configuration
public class MybatisMapperScannerAutoConfiguration {

    private final MybatisProperties mybatisProperties;

    public MybatisMapperScannerAutoConfiguration(MybatisProperties mybatisProperties) {
        this.mybatisProperties = mybatisProperties;
    }

    @Primary
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Qualifier("sqlSessionFactory") SqlSessionFactoryBean sessionFactoryBean) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisProperties.getMapperPackage());
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }
}
