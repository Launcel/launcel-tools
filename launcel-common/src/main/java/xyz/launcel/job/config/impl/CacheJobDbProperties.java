package xyz.launcel.job.config.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.job.config.JobDbConfig;

import java.util.Objects;

@Data
@ConfigurationProperties(prefix = "job.datasource")
public class CacheJobDbProperties implements JobDbConfig
{
    private Boolean enabled     = false;
    private String  tableName;
    private String  driverClass = "com.mysql.jdbc.Driver";
    private String  user;
    private String  password;
    private String  url;

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : false;
    }
}
