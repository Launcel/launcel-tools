package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.lang.Base64;
import xyz.launcel.lang.StringUtils;

import java.util.Map;
import java.util.Properties;

/**
 * Created by xuyang in 2017/9/19
 */
@ConfigurationProperties(prefix = "db.custom.jdbc")
public class CustomDataSourceProperties {


    private Map<String, CustomHikariDataSource> list;

    public Map<String, CustomHikariDataSource> getList() {
        return list;
    }

    public void setList(Map<String, CustomHikariDataSource> list) {
        this.list = list;
    }

    public static class CustomHikariDataSource {

        private String name = "custom" + StringUtils.random(3);

        private String driverName = "com.mysql.jdbc.Driver";

        private String jdbcUrl = "jdbc:mysql://localhost:3306/test";

        private String username = "root";

        private String password = "123456";

        private Integer minIdle = 1;

        private Integer maxPoolSize = 5;

        private Long idleTimeout = 600000L;

        private Long maxLifetime = 1800000L;

        private Long connectionTimeout = 30000L;

        private String connectionTestQuery = "select 'x'";

        private Properties dataSourceProperties;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
            config.setDriverClassName(getDriverName());
            config.setPassword(Base64.decode(getPassword()));
            config.setJdbcUrl(getJdbcUrl());
            config.setUsername(getUsername());
            config.setMinimumIdle(getMinIdle());
            config.setMaximumPoolSize(getMaxPoolSize());
            config.setMaxLifetime(getMaxLifetime());
            config.setIdleTimeout(getIdleTimeout());
            config.setConnectionTimeout(getConnectionTimeout());
            config.setConnectionTestQuery(getConnectionTestQuery());
            if (!getDataSourceProperties().isEmpty())
                config.setDataSourceProperties(getDataSourceProperties());
            return config;
        }
    }


}
