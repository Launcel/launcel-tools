package xyz.launcel.properties;

import javax.sql.DataSource;

public class RoleDataSourceHolder {
    
    private DataSource dataSource;
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
