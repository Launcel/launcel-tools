package xyz.x.jdbc;

import xyz.launcel.common.SpringBeanUtil;

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
