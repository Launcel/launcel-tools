package xyz.x.common.job.config.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.x.common.job.config.JobDbConfig;

@Data
@ConfigurationProperties(prefix = "job.datasource")
public class CacheJobDbProperties implements JobDbConfig
{
    private String tableName;
    private String driverClass = "com.mysql.jdbc.Driver";
    private String user;
    private String password;
    private String url;

}
