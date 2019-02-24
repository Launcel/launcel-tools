package xyz.launcel.job.config;

public interface JobDbConfig
{
    String getTableName();

    String getDriverClass();

    String getUser();

    String getPassword();

    String getUrl();
}
