package xyz.launcel.prop;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class RoleDataSourceHolder
{

    private DataSource dataSource;

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
}
