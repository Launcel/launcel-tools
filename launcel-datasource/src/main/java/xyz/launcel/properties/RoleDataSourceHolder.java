package xyz.launcel.properties;

import javax.sql.DataSource;

public class RoleDataSourceHolder {
    
    private DataSource hikariDataSource;
    
    public DataSource getHikariDataSource() {
        return hikariDataSource;
    }
    
    public void setHikariDataSource(DataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }
}
