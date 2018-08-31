package xyz.launcel.holder;

/**
 * Created by launcel on 2018/8/20.
 */
public class DbContextHolder
{
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置数据源
     */
    public static void setDbType(String dataSourceName)
    {
        contextHolder.set(dataSourceName);
    }

    /**
     * 取得当前数据源
     */
    public static String getDbType()
    {
        return contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    private static void clearDbType()
    {
        contextHolder.remove();
    }
}
