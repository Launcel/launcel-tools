package xyz.launcel.jdbc;

import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcRole extends BaseLogger {

    private static String DEFAULT_AUTHENTICATION_QUERY = "select password from users where username = ?";

    private static String DEFAULT_USER_ROLES_QUERY = "select role_name from user_roles where username = ?";

    protected String authenticationQuery = DEFAULT_AUTHENTICATION_QUERY;

    protected String userRoleQuery = DEFAULT_USER_ROLES_QUERY;

    private DataSource dataSource;

    public Set<String> getRoles(String username) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return getRoleNamesForUser(conn, username);
        } catch (SQLException e) {
            if (isDebugEnabled())
                debug(e.getMessage());
            return null;
        } finally {
            close(conn);
        }
    }

    @SuppressWarnings("unchecked")
    protected Set<String> getRoleNamesForUser(Connection conn, String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> roleNames = new LinkedHashSet<>();
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
        }
        return roleNames;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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
                if (isDebugEnabled())
                    debug("Could not close JDBC Connection", e);
            }
        }
    }

    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                if (isDebugEnabled())
                    debug("Could not close JDBC PreparedStatement", e);
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                if (isDebugEnabled())
                    debug("Could not close JDBC ResultSet", e);
            }
        }
    }
}
