package xyz.launcel.jdbc;

import lombok.Getter;
import lombok.Setter;
import xyz.launcel.log.Log;
import xyz.launcel.redis.properties.RoleDataSourceHolder;
import xyz.launcel.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class JdbcRole
{

    private String authenticationQuery = "select password from users where username = ?";
    private String userRoleQuery       = "select role_name from user_roles where username = ?";

    private Connection connection;

    public Set<String> getRoles(String username)
    {
        try
        {
            connection = RoleDataSourceHolder.getDataSource().getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return getRoleNamesForUser(connection, username);
    }

    @SuppressWarnings("unchecked")
    protected Set<String> getRoleNamesForUser(Connection conn, String username)
    {
        PreparedStatement ps        = null;
        ResultSet         rs        = null;
        Set<String>       roleNames = new LinkedHashSet<>();
        try
        {
            ps = conn.prepareStatement(getUserRoleQuery());
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next())
            {
                String roleName = rs.getString(1);
                if (StringUtils.isNotBlank(roleName))
                {
                    roleNames.addAll((Set<String>) StringUtils.spiltStream(roleName, ";").collect(Collectors.toSet()));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            close(rs);
            close(ps);
            close(conn);
        }
        return roleNames;
    }

    private void close(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                if (Log.isDebug())
                {
                    Log.debug("Could not close JDBC Connection");
                }
            }
        }
    }

    private void close(PreparedStatement ps)
    {
        if (ps != null)
        {
            try
            {
                ps.close();
            }
            catch (SQLException e)
            {
                if (Log.isDebug())
                {
                    Log.debug("Could not close JDBC PreparedStatement");
                }
            }
        }
    }

    private void close(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                if (Log.isDebug())
                {
                    Log.debug("Could not close JDBC ResultSet");
                }
            }
        }
    }
}
