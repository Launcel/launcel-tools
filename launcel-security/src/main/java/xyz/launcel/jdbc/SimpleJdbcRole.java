package xyz.launcel.jdbc;

import xyz.launcel.autoconfig.SpringBeanUtil;

public class SimpleJdbcRole
{

    private static JdbcRole jdbcRole = null;

    static
    {
        jdbcRole = SpringBeanUtil.getBean("jdbcRole");
    }

    public static JdbcRole getJdbcRole()
    {
        return jdbcRole;
    }
}
