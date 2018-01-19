package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.lang.Base64;

import java.util.Properties;

/**
 * Created by xuyang in 2017/9/19
 */
@ConfigurationProperties(prefix = "db.jdbc")
public class DataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer minIdle;
    private Integer maxPoolSize;
    private Long idleTimeout;
    private Long maxLifeTime;
    private Long connectionTimeout;
    private String connectionTestQuery;
    private Properties dataSourceProperty;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Long getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(Long maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public Properties getDataSourceProperty() {
        return dataSourceProperty;
    }

    public void setDataSourceProperty(Properties dataSourceProperty) {
        this.dataSourceProperty = dataSourceProperty;
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setPassword(Base64.decode(password));
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMaxLifetime(maxLifeTime);
        config.setIdleTimeout(idleTimeout);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery(connectionTestQuery);
        if (dataSourceProperty != null && !dataSourceProperty.isEmpty())
            config.setDataSourceProperties(dataSourceProperty);
        return config;
    }

}
