package xyz.launcel.redis.properties;

import javax.sql.DataSource;

public class RoleDataSourceHolder
{

    private static DataSource dataSource;

    public static DataSource getDataSource()
    {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource)
    {
        RoleDataSourceHolder.dataSource = dataSource;
    }
}
