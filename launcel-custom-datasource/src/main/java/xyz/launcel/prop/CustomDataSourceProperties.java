package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.lang.Base64;
import xyz.launcel.lang.StringUtils;

import java.util.List;
import java.util.Properties;

/**
 * Created by xuyang in 2017/9/19
 */
@ConfigurationProperties(prefix = "custom.jdbc")
public class CustomDataSourceProperties {


    private List<PrimyHikariDataSource> list;

    public List<PrimyHikariDataSource> getList() {
        return list;
    }

    public void setList(List<PrimyHikariDataSource> list) {
        this.list = list;
    }

    public static class PrimyHikariDataSource {

        private String name = "custom" + StringUtils.random(3);

        private String driverClassName = "com.mysql.jdbc.Driver";

        private String url = "jdbc:mysql://localhost:3306/test";

        private String username = "root";

        private String password = "123456";

        private Integer minIdle = 1;

        private Integer maxPoolSize = 5;

        private Long idleTimeout = 600000L;

        private Long maxLifetime = 1800000L;

        private Long connectionTimeout = 30000L;

        private String connectionTestQuery = "select 'x'";

        private Properties dataSourceProp;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public Properties getDataSourceProp() {
            return dataSourceProp;
        }

        public void setDataSourceProp(Properties dataSourceProp) {
            this.dataSourceProp = dataSourceProp;
        }

        public HikariConfig getHikariConfig() {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(getDriverClassName());
            config.setPassword(Base64.decode(getPassword()));
            config.setJdbcUrl(getUrl());
            config.setUsername(getUsername());
            config.setMinimumIdle(getMinIdle());
            config.setMaximumPoolSize(getMaxPoolSize());
            config.setMaxLifetime(getMaxLifetime());
            config.setIdleTimeout(getIdleTimeout());
            config.setConnectionTimeout(getConnectionTimeout());
            config.setConnectionTestQuery(getConnectionTestQuery());
            if (!getDataSourceProp().isEmpty())
                config.setDataSourceProperties(getDataSourceProp());
            return config;
        }
    }


}
