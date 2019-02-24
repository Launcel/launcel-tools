package xyz.launcel.job.config.impl;

import lombok.Getter;
import lombok.Setter;
import xyz.launcel.job.config.JobDbConfig;

@Getter
@Setter
public class CacheJobDbConfig implements JobDbConfig
{
    private String tableName;
    private String driverClass;
    private String user;
    private String password;
    private String url;
}
