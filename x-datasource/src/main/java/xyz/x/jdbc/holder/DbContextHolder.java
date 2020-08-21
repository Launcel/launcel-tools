package xyz.x.jdbc.holder;

/**
 * Created by launcel on 2018/8/20.
 */
public class DbContextHolder
{
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 取得当前数据源
     */
    static String getDbType()
    {
        return contextHolder.get();
    }

    /**
     * 设置数据源
     */
    public static void setDbType(String dataSourceName)
    {
        contextHolder.set(dataSourceName);
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType()
    {
        contextHolder.remove();
    }
}
