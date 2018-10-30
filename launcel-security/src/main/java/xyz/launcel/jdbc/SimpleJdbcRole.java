package xyz.launcel.jdbc;

import xyz.launcel.bean.context.SpringBeanUtil;

import java.util.Objects;

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
