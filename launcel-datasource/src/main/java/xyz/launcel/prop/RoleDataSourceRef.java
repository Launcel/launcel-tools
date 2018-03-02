package xyz.launcel.prop;

import com.zaxxer.hikari.HikariDataSource;

public class RoleDataSourceRef {

    private HikariDataSource hikariDataSource;

    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    public void setHikariDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }
}
