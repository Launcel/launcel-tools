package xyz.launcel.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import xyz.launcel.properties.LogProperties;

//@Configuration
@EnableConfigurationProperties(value = LogProperties.class)
public class LogAutoConfiguration
{}
