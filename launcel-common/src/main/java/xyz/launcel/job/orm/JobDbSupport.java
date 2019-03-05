package xyz.launcel.job.orm;

import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.commonName.BeanNameList;
import xyz.launcel.ensure.Me;
import xyz.launcel.job.config.JobDbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobDbSupport
{
    private static JobDbConfig jobDbConfig;
    static
    {
        init();
    }
    private static void init()
    {
        jobDbConfig = SpringBeanUtil.getBean(BeanNameList.jobDbConfig);
        try
        {
            Class.forName(jobDbConfig.getDriverClass()).getDeclaredConstructor().newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }
    }

    private static Connection getConn()
    {
        try
        {
            return DriverManager.getConnection(jobDbConfig.getUrl(), jobDbConfig.getUser(), jobDbConfig.getPassword());
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
            var conn = getConn();
            Me.builder(conn).isNull("create Connection error!!!");
            var pstmt = conn.prepareStatement(sql);
            for (int i = 1; i < objects.length + 1; i++)
            {
                pstmt.setObject(i, objects[i - 1]);
            }
            var rs = pstmt.executeQuery();
            while (rs.next())
            {
                var entity = ScheduleJobEntity.builder().jobName(rs.getString("job_name")).cron(rs.getString("cron")).status(rs.getShort("status")).build();
                entity.setId(rs.getInt("id"));
                entity.setEnabled(rs.getBoolean("enabled"));
                list.add(entity);
            }
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static int add(ScheduleJobEntity entity)
    {
        var conn = getConn();
        Me.builder(conn).isNull("create Connection error!!!");
        int    result = 0;
        String sql    = "insert into " + jobDbConfig.getTableName() + "( job_name, cron, status, create_time, create_user, enabled) values(?, ?, ?, ?, ?, 1)";
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
        var conn = getConn();
        Me.builder(conn).isNull("create Connection error!!!");
        int    result = 0;
        String sql    = "update " + getTableName() + " set job_name=?, cron=?, status=?, update_time=?, update_user=?, enabled=?";
        try
        {
            var ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getJobName());
            ps.setString(2, entity.getCron());
            ps.setShort(3, entity.getStatus());
            ps.setObject(4, new Date());
            ps.setString(5, entity.getUpdateUser());
            ps.setBoolean(6, entity.getEnabled());
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

    public static String getTableName()
    {
        return jobDbConfig.getTableName();
    }
}
