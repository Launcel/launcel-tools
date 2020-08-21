package xyz.x.jdbc.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.var;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.x.jdbc.SessionFactoryConstant;
import xyz.launcel.common.utils.Base64;

import java.util.List;
import java.util.Properties;

/**
 * Created by Launcel in 2017/9/19
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SessionFactoryConstant.dataSourceConfigPrefix)
public class DataSourceProperties
{

    private Boolean useDynamicDataSource = false;

    @NonNull
    private DataSourcePropertie main;

    private List<DataSourcePropertie> others;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DataSourcePropertie
    {
        private String  name                  = "main";
        // private String  driverClassName     = "net.sf.log4jdbc.DriverSpy";
        private String  driverClass           = "org.mariadb.jdbc.Driver";
        // private String  url                 = "jdbc:log4jdbc:mysql://localhost:3306/wx-shop?autoReconnect=true&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&failOverReadOnly=false";
        @NonNull
        private String  url                   = "jdbc:mariadb://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&failOverReadOnly=false";
        private String  username              = "root";
        @NonNull
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

        public HikariConfig getHikariConfig()
        {
            var config = new HikariConfig();
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

            var dataSourceProperty = new Properties();
            dataSourceProperty.put("dataSource.cachePrepStmts", getCachePrepStmts());
            dataSourceProperty.put("dataSource.prepStmtCacheSize", getPrepStmtCacheSize());
            dataSourceProperty.put("dataSource.prepStmtCacheSqlLimit", getPrepStmtCacheSqlLimit());
            dataSourceProperty.put("dataSource.useServerPrepStmts", getUseServerPrepStmts());
            config.setDataSourceProperties(dataSourceProperty);
            return config;
        }
    }
}
