package xyz.launcel.jdbc;

import xyz.launcel.constant.SessionConstant;
import xyz.launcel.hook.ApplicationContextHook;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.properties.RoleDataSourceHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcRole extends BaseLogger {
    
    protected String authenticationQuery = "select password from users where username = ?";
    protected String userRoleQuery       = "select role_name from user_roles where username = ?";
    
    private Connection connection;
    
    public Set<String> getRoles(String username) {
        try {
            connection = ((RoleDataSourceHolder) ApplicationContextHook.getBean(SessionConstant.roleDateSourceName)).getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getRoleNamesForUser(connection, username);
    }
    
    @SuppressWarnings("unchecked")
    protected Set<String> getRoleNamesForUser(Connection conn, String username) {
        PreparedStatement ps        = null;
        ResultSet         rs        = null;
        Set<String>       roleNames = new LinkedHashSet<>();
        try {
            ps = conn.prepareStatement(userRoleQuery);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                String roleName = rs.getString(1);
                if (StringUtils.isNotBlank(roleName)) {
                    roleNames.addAll((Set<String>) StringUtils.spiltStream(roleName, ";").collect(Collectors.toSet()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return roleNames;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public String getUserRoleQuery() {
        return userRoleQuery;
    }
    
    public void setUserRoleQuery(String userRoleQuery) {
        this.userRoleQuery = userRoleQuery;
    }
    
    public String getAuthenticationQuery() {
        return authenticationQuery;
    }
    
    public void setAuthenticationQuery(String authenticationQuery) {
        this.authenticationQuery = authenticationQuery;
    }
    
    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                if (isDebug())
                    DEBUG("Could not close JDBC Connection");
            }
        }
    }
    
    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                if (isDebug())
                    DEBUG("Could not close JDBC PreparedStatement");
            }
        }
    }
    
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                if (isDebug())
                    DEBUG("Could not close JDBC ResultSet");
            }
        }
    }
}
