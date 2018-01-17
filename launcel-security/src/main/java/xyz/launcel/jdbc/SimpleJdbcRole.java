package xyz.launcel.jdbc;

import xyz.launcel.hook.ApplicationContextHook;

public class SimpleJdbcRole {

    private static JdbcRole jdbcRole = null;

    static {
        if (ApplicationContextHook.hasBean("jdbcRole"))
            jdbcRole = (JdbcRole) ApplicationContextHook.getBean("jdbcRole");
    }

    public static JdbcRole getJdbcRole() {
        return jdbcRole;
    }
}
