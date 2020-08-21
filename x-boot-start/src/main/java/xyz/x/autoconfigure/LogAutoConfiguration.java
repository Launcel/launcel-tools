package xyz.x.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import xyz.x.properties.LogProperties;

//@Configuration
@EnableConfigurationProperties(value = LogProperties.class)
public class LogAutoConfiguration
{}
