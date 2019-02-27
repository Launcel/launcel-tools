package xyz.launcel.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "job.datasource")
public class JobDatasourceProperties
{
    private Boolean enabled     = false;
    @NonNull
    private String  tableName;
    @NonNull
    private String  driverClass = "com.mysql.jdbc.Driver";
    @NonNull
    private String  user;
    @NonNull
    private String  password;
    @NonNull
    private String  url;
}
