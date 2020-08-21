package xyz.launcel.jdbc.holder;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by launcel on 2018/8/29.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DataSourceConfigMap
{
    private String           name;
    private Boolean          enableTransactal;
    private Boolean          roleDataSource;
    private HikariDataSource dataSource;
}
