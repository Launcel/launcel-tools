package xyz.launcel.jdbc;

import xyz.launcel.bean.context.SpringBeanUtil;

import java.util.Objects;

public class SimpleJdbcRole
{

    private static JdbcRole jdbcRole = null;

    static
    {
        if (SpringBeanUtil.hasBean("jdbcRole") && Objects.nonNull(SpringBeanUtil.getBean("jdbcRole")))
            jdbcRole = SpringBeanUtil.getBean("jdbcRole");
    }

    public static JdbcRole getJdbcRole()
    {
        return jdbcRole;
    }
}
