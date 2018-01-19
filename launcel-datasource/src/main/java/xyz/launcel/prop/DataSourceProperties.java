package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xyz.launcel.lang.Base64;

import java.util.Properties;

/**
 * Created by xuyang in 2017/9/19
 */
@Configuration
//@ConfigurationProperties(prefix = "db.jdbc")
public class DataSourceProperties {

    @Value("${db.jdbc.driver-name}")
    private String driverName;
    @Value("${db.jdbc.jdbc-url}")
    private String jdbcUrl;
    @Value("${db.jdbc.driver-name}")
    private String username;
    @Value("${db.jdbc.driver-name}")
    private String password;
    @Value("${db.jdbc.min-idle}")
    private Integer minIdle;
    @Value("${db.jdbc.max-pool-size}")
    private Integer maxPoolSize;
    @Value("${db.jdbc.idle-timeout}")
    private Long idleTimeout;
    @Value("${db.jdbc.max-life-time}")
    private Long maxLifeTime;
    @Value("${db.jdbc.connection-timeout")
    private Long connectionTimeout;
    @Value("${db.jdbc.connection-test-query}")
    private String connectionTestQuery;
    @Value("${db.jdbc.data-source-properties}")
    private Properties dataSourceProperties;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public Properties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public String toString() {
        return "DataSourceProperties[" +
                "driverName='" + driverName + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", minIdle=" + minIdle +
                ", maxPoolSize=" + maxPoolSize +
                ", idleTimeout=" + idleTimeout +
                ", maxLifeTime=" + maxLifeTime +
                ", connectionTimeout=" + connectionTimeout +
                ", connectionTestQuery='" + connectionTestQuery + '\'' +
                ", dataSourceProperties=" + dataSourceProperties +
                ']';
    }

    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info(toString());
        config.setDriverClassName(getDriverName());
        config.setPassword(Base64.decode(getPassword()));
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setMinimumIdle(getMinIdle());
        config.setMaximumPoolSize(getMaxPoolSize());
        config.setMaxLifetime(getMaxLifeTime());
        config.setIdleTimeout(getIdleTimeout());
        config.setConnectionTimeout(getConnectionTimeout());
        config.setConnectionTestQuery(getConnectionTestQuery());
        if (dataSourceProperties != null && !getDataSourceProperties().isEmpty())
            config.setDataSourceProperties(getDataSourceProperties());
        return config;
    }

}
