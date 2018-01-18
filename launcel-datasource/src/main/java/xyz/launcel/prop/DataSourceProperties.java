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

    private String jdbcUrl;

    private String username;

    private String password;

    private Integer minIdle;

    private Integer maxPoolSize;

    private Long idleTimeout;

    private Long maxLifetime;

    private Long connectionTimeout;

    private String connectionTestQuery;

    private Properties dataSourceProperties;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
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

    public Long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(Long maxLifetime) {
        this.maxLifetime = maxLifetime;
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

    public Properties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(getDriverClassName());
        config.setPassword(Base64.decode(getPassword()));
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setMinimumIdle(getMinIdle());
        config.setMaximumPoolSize(getMaxPoolSize());
        config.setMaxLifetime(getMaxLifetime());
        config.setIdleTimeout(getIdleTimeout());
        config.setConnectionTimeout(getConnectionTimeout());
        config.setConnectionTestQuery(getConnectionTestQuery());
        config.setDataSourceProperties(getDataSourceProperties());
//        if (isDebugEnabled())
//        debug("\n---------------------------------------------------------------\thikariConfig is : {}", Json.toJson(config) + "\n---------------------------------------------------------------");
        return config;
    }

}
