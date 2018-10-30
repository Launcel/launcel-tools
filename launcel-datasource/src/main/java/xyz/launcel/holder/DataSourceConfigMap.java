package xyz.launcel.holder;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Created by launcel on 2018/8/29.
 */
@Getter
@Setter
public class DataSourceConfigMap
{
    private String           name;
    private Boolean          enableTransactal;
    private Boolean          roleDataSource;
    private HikariDataSource dataSource;

    DataSourceConfigMap()
    {
    }

    DataSourceConfigMap(String name, Boolean enableTransactal, Boolean roleDataSource, HikariDataSource dataSource)
    {
        this.name = name;
        this.enableTransactal = enableTransactal;
        this.roleDataSource = roleDataSource;
        this.dataSource = dataSource;
    }
}
