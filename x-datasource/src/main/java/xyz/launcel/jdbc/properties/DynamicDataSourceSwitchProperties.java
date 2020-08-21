package xyz.launcel.jdbc.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {DataSourceProperties.class, MybatisProperties.class})
public class DynamicDataSourceSwitchProperties
{}
