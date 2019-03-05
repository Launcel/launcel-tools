package xyz.launcel.job.config.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.job.config.JobDbConfig;

@Getter
@Setter
@ConfigurationProperties(prefix = "job.datasource")
public class CacheJobDbProperties implements JobDbConfig
{
    private Boolean enabled     = false;
    private String  tableName;
    private String  driverClass = "com.mysql.jdbc.Driver";
    private String  user;
    private String  password;
    private String  url;
}
