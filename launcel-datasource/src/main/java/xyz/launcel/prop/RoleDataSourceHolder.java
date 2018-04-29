package xyz.launcel.prop;

import com.zaxxer.hikari.HikariDataSource;

public class RoleDataSourceHolder {
    
    private HikariDataSource hikariDataSource;
    
    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }
    
    public void setHikariDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }
}
