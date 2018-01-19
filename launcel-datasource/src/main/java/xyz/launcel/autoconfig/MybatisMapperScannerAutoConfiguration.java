/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.19
 * Version: 1.0
 */

package xyz.launcel.autoconfig;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.launcel.prop.MybatisProperties;

@Configuration
@AutoConfigureAfter(SessionFactoryAutoConfiguration.class)
public class MybatisMapperScannerAutoConfiguration {

    private final MybatisProperties mybatisProperties;

    public MybatisMapperScannerAutoConfiguration(MybatisProperties mybatisProperties) {
        this.mybatisProperties = mybatisProperties;
    }

    @Primary
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(mybatisProperties.getMapperPackage());
        return mapperScannerConfigurer;
    }
}
