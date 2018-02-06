package xyz.launcel.jdbc;

import xyz.launcel.hook.ApplicationContextHook;

import java.util.Objects;

public class SimpleJdbcRole {

    private static JdbcRole jdbcRole = null;

    static {
        if (ApplicationContextHook.hasBean("jdbcRole") &&
                Objects.nonNull(ApplicationContextHook.getBean("jdbcRole")))
            jdbcRole = (JdbcRole) ApplicationContextHook.getBean("jdbcRole");
    }

    public static JdbcRole getJdbcRole() {
        return jdbcRole;
    }
}
