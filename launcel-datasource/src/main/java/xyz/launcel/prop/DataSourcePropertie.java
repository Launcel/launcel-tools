package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.lang.Base64;
import xyz.launcel.lang.Json;

import java.util.Properties;

/**
 * Created by xuyang in 2017/9/19
 */
@ConfigurationProperties(prefix = "db.jdbc")
public class DataSourcePropertie {

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
    private Properties dataSourceProp;

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

    public Properties getDataSourceProp() {
        return dataSourceProp;
    }

    public void setDataSourceProp(Properties dataSourceProp) {
        this.dataSourceProp = dataSourceProp;
    }

    @Override
    public String toString() {
        return "DataSourcePropertie : [\n" +
                "\t'driverName' : '" + driverClassName + '\'' +
                "\n\t, 'jdbcUrl' : '" + url + '\'' +
                "\n\t, 'username' : '" + username + '\'' +
                "\n\t, 'password' : '" + password + '\'' +
                "\n\t, 'minIdle' : '" + minIdle + '\'' +
                "\n\t, 'maxPoolSize' : '" + maxPoolSize + '\'' +
                "\n\t, 'idleTimeout' : '" + idleTimeout + '\'' +
                "\n\t, 'maxLifeTime' : '" + maxLifeTime + '\'' +
                "\n\t, 'connectionTimeout' : '" + connectionTimeout + '\'' +
                "\n\t, 'connectionTestQuery' : '" + connectionTestQuery + '\'' +
                "\n\t, 'dataSourceProp' : '" + dataSourceProp + '\'' +
                ']';
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info(Json.toJson(toString()));
        config.setDriverClassName(getDriverClassName());
        config.setPassword(Base64.decode(getPassword()));
        config.setJdbcUrl(getUrl());
        config.setUsername(getUsername());
        config.setMinimumIdle(getMinIdle());
        config.setMaximumPoolSize(getMaxPoolSize());
        config.setMaxLifetime(getMaxLifeTime());
        config.setIdleTimeout(getIdleTimeout());
        config.setConnectionTimeout(getConnectionTimeout());
        config.setConnectionTestQuery(getConnectionTestQuery());
        if (dataSourceProp != null && !getDataSourceProp().isEmpty())
            config.setDataSourceProperties(getDataSourceProp());
        return config;
    }

}
