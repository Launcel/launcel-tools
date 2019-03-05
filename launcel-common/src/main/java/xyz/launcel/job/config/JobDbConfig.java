package xyz.launcel.job.config;

import org.springframework.lang.NonNull;
public interface JobDbConfig
{
    @NonNull
    String getTableName();
    @NonNull
    String getDriverClass();
    @NonNull
    String getUser();
    @NonNull
    String getPassword();
    @NonNull
    String getUrl();
}
