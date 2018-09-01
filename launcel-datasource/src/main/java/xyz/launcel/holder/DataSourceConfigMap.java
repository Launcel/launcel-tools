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
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
class DataSourceConfigMap
{
    private String           name;
    private Boolean          enableTransactal;
    private Boolean          roleDataSource;
    private HikariDataSource dataSource;
}
