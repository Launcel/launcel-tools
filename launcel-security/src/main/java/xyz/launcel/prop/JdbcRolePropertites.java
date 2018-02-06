/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.6
 * Version: 1.0
 */

package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web.role")
public class JdbcRolePropertites {
    private String dataSourceName;
    private String authenticationQuery;
    private String userRoleQuery;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getAuthenticationQuery() {
        return authenticationQuery;
    }

    public void setAuthenticationQuery(String authenticationQuery) {
        this.authenticationQuery = authenticationQuery;
    }

    public String getUserRoleQuery() {
        return userRoleQuery;
    }

    public void setUserRoleQuery(String userRoleQuery) {
        this.userRoleQuery = userRoleQuery;
    }
}
