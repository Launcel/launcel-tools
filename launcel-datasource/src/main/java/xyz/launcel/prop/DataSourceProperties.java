package xyz.launcel.prop;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.constant.SessionConstant;
import xyz.launcel.lang.Base64;

import java.util.List;
import java.util.Properties;

/**
 * Created by Launcel in 2017/9/19
 */
@ConfigurationProperties(prefix = SessionConstant.dataSourceConfigPrefix)
public class DataSourceProperties {

    private DataSourcePropertie main;

    private List<DataSourcePropertie> others;


    public DataSourcePropertie getMain() {
        return main;
    }

    public void setMain(DataSourcePropertie main) {
        this.main = main;
    }

    public List<DataSourcePropertie> getOthers() {
        return others;
    }

    public void setOthers(List<DataSourcePropertie> others) {
        this.others = others;
    }

    public static class DataSourcePropertie {
        private String name = "main";
        private String driverClassName = "net.sf.log4jdbc.DriverSpy";
        private String url = "jdbc:log4jdbc:mysql://localhost:3306/test";
        private String username = "root";
        private String password = "MTIzNDU2";
        private Integer minIdle = 1;
        private Integer maxPoolSize = 5;
        private Long idleTimeout = 600000L;
        private Long maxLifeTime = 1800000L;
        private Long connectionTimeout = 30000L;
        private String connectionTestQuery = "select 'x'";
        private Boolean enabledTransactal = false;
        private Properties dataSourceProperty = new Properties() {{
            put("dataSource.cachePrepStmts", true);
            put("dataSource.prepStmtCacheSize", 250);
            put("dataSource.prepStmtCacheSqlLimit", 2048);
            put("dataSource.useServerPrepStmts", true);
        }};

        private Boolean isRead = false;

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

        public Boolean getEnabledTransactal() {
            return enabledTransactal;
        }

        public void setEnabledTransactal(Boolean enabledTransactal) {
            this.enabledTransactal = enabledTransactal;
        }

        public Boolean getRead() {
            return isRead;
        }

        public void setRead(Boolean read) {
            isRead = read;
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
            config.setReadOnly(isRead);
            if (dataSourceProperty != null && !dataSourceProperty.isEmpty())
                config.setDataSourceProperties(dataSourceProperty);
            return config;
        }
    }
}
