package xyz.launcel.properties;

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
        private String  name                  = "main";
        // private String  driverClassName     = "net.sf.log4jdbc.DriverSpy";
        private String  driverClass       = "org.mariadb.jdbc.Driver";
        // private String  url                 = "jdbc:log4jdbc:mysql://localhost:3306/wx-shop?autoReconnect=true&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&failOverReadOnly=false";
        private String  url                   = "jdbc:mariadb://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&failOverReadOnly=false";
        private String  username              = "root";
        private String  password              = "MTIzNDU2";
        private Integer minIdle               = 1;
        // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
        private Integer maxPoolSize           = 5;
        // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
        private Long    idleTimeout           = 600000L;
        // 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒
        private Long    maxLifeTime           = 1800000L;
        // 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';）
        private Long    connectionTimeout     = 30000L;
        private String  connectionTestQuery   = "select 'x'";
        private Boolean useServerPrepStmts    = true;
        private Boolean cachePrepStmts        = true;
        private Integer prepStmtCacheSize     = 250;
        private Integer prepStmtCacheSqlLimit = 2048;
        // 只读数据库时配置为true
        private Boolean read                  = false;
        private Boolean enableTransactal      = false;
        private Boolean roleDataSource        = false;
        private Boolean debugSql              = false;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDriverClass() {
            return driverClass;
        }
        
        public void setDriverClass(String driverClassName) {
            this.driverClass = driverClassName;
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
        
        public Boolean getEnableTransactal() {
            return enableTransactal;
        }
        
        public void setEnableTransactal(Boolean enableTransactal) {
            this.enableTransactal = enableTransactal;
        }
        
        public Boolean getRead() {
            return read;
        }
        
        public void setRead(Boolean read) {
            this.read = read;
        }
        
        public Boolean getRoleDataSource() {
            return roleDataSource;
        }
        
        public void setRoleDataSource(Boolean roleDataSource) {
            this.roleDataSource = roleDataSource;
        }
        
        public Boolean getCachePrepStmts() {
            return cachePrepStmts;
        }
        
        public void setCachePrepStmts(Boolean cachePrepStmts) {
            this.cachePrepStmts = cachePrepStmts;
        }
        
        public Integer getPrepStmtCacheSize() {
            return prepStmtCacheSize;
        }
        
        public void setPrepStmtCacheSize(Integer prepStmtCacheSize) {
            this.prepStmtCacheSize = prepStmtCacheSize;
        }
        
        public Integer getPrepStmtCacheSqlLimit() {
            return prepStmtCacheSqlLimit;
        }
        
        public void setPrepStmtCacheSqlLimit(Integer prepStmtCacheSqlLimit) {
            this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
        }
        
        public Boolean getUseServerPrepStmts() {
            return useServerPrepStmts;
        }
        
        public void setUseServerPrepStmts(Boolean useServerPrepStmts) {
            this.useServerPrepStmts = useServerPrepStmts;
        }
        
        public Boolean getDebugSql() {
            return debugSql;
        }
        
        public void setDebugSql(Boolean debugSql) {
            this.debugSql = debugSql;
        }
        
        public HikariConfig getHikariConfig() {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(getDriverClass());
            config.setPassword(Base64.decode(getPassword()));
            config.setJdbcUrl(getUrl());
            config.setUsername(getUsername());
            config.setMinimumIdle(getMinIdle());
            config.setMaximumPoolSize(getMaxPoolSize());
            config.setMaxLifetime(getMaxLifeTime());
            config.setIdleTimeout(getIdleTimeout());
            config.setConnectionTimeout(getConnectionTimeout());
            config.setConnectionTestQuery(getConnectionTestQuery());
            config.setReadOnly(getRead());
            
            Properties dataSourceProperty = new Properties();
            dataSourceProperty.put("dataSource.cachePrepStmts", getCachePrepStmts());
            dataSourceProperty.put("dataSource.prepStmtCacheSize", getPrepStmtCacheSize());
            dataSourceProperty.put("dataSource.prepStmtCacheSqlLimit", getPrepStmtCacheSqlLimit());
            dataSourceProperty.put("dataSource.useServerPrepStmts", getUseServerPrepStmts());
            config.setDataSourceProperties(dataSourceProperty);
            return config;
        }
    }
}
