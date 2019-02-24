package xyz.launcel.job.orm;

import lombok.var;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.job.config.JobDbConfig;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class JobDbSupport
{
    private static JobDbConfig jobDbConfig = SpringBeanUtil.getBean(JobDbConfig.class);

    private static Driver driver;

    private static Properties info;

    private static Driver getDriver()
    {
        if (driver == null)
            synchronized (JobDbSupport.class)
            {
                if (driver == null)
                {
                    try
                    {
                        driver = (Driver) Class.forName(jobDbConfig.getDriverClass()).getDeclaringClass().newInstance();
                        info = new Properties();
                        info.put("user", jobDbConfig.getUser());
                        info.put("password", jobDbConfig.getPassword());
                    }
                    catch (ReflectiveOperationException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        return driver;
    }

    private static Connection getConn()
    {
        try
        {
            return getDriver().connect(jobDbConfig.getUrl(), info);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ScheduleJobEntity> query(String sql, Object[] objects)
    {
        var list = new ArrayList<ScheduleJobEntity>();
        try
        {
            var conn  = getConn();
            var pstmt = conn.prepareStatement(sql);
            for (int i = 1; i < objects.length + 1; i++)
            {
                pstmt.setObject(i, objects[i - 1]);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
                    var entity = new ScheduleJobEntity();
                    entity.setId(rs.getInt("id"));
                    entity.setJobName(rs.getString("job_name"));
                    entity.setCron(rs.getString("cron"));
                    entity.setStatus(rs.getShort("status"));
                    list.add(entity);
                }
                rs.close();
                pstmt.close();
                conn.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static int add(ScheduleJobEntity entity)
    {
        var    conn   = getConn();
        int    result = 0;
        String sql    = "insert into " + jobDbConfig.getTableName() + "( job_name, cron, status, create_time, create_user) values(?, ?, ?, ?, ?)";
        try
        {
            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entity.getJobName());
            pstmt.setString(2, entity.getCron());
            pstmt.setShort(3, entity.getStatus());
            pstmt.setObject(4, new Date());
            pstmt.setString(5, entity.getCreateUser());
            result = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static int update(ScheduleJobEntity entity)
    {
        var    conn   = getConn();
        int    result = 0;
        String sql    = "update " + jobDbConfig.getTableName() + " set job_name=?, cron=?, status=?, update_time=?, update_user=?";
        try
        {
            var ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getJobName());
            ps.setString(2, entity.getCron());
            ps.setShort(3, entity.getStatus());
            ps.setObject(4, new Date());
            ps.setString(5, entity.getUpdateUser());
            result = ps.executeUpdate();
            ps.close();
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
