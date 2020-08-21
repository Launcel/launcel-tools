package xyz.x.jdbc.interceptor;

import com.github.pagehelper.Dialect;
import com.github.pagehelper.PageException;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PageAutoDialect extends com.github.pagehelper.page.PageAutoDialect
{

    //自动获取dialect,如果没有setProperties或setSqlUtilConfig，也可以正常进行
    private boolean               autoDialect = true;
    //多数据源时，获取jdbcurl后是否关闭数据源
    private boolean               closeConn   = true;
    //属性配置
    private Properties            properties;
    private AbstractHelperDialect delegate;

    private static Map<String, Class<? extends Dialect>> dialectAliasMap = new HashMap<String, Class<? extends Dialect>>();

    public static void registerDialectAlias(String alias, Class<? extends Dialect> dialectClass)
    {
        dialectAliasMap.put(alias, dialectClass);
    }

    static
    {
        //注册别名
        registerDialectAlias("mysql", MySqlDialect.class);
        registerDialectAlias("mariadb", MySqlDialect.class);
    }

    /**
     * 初始化 helper
     *
     * @param dialectClass
     * @param properties
     */
    private AbstractHelperDialect initDialect(String dialectClass, Properties properties)
    {
        AbstractHelperDialect dialect;
        if (StringUtil.isEmpty(dialectClass))
        {
            throw new PageException("使用 PageHelper 分页插件时，必须设置 helper 属性");
        }
        try
        {
            Class sqlDialectClass = resloveDialectClass(dialectClass);
            if (AbstractHelperDialect.class.isAssignableFrom(sqlDialectClass))
            {
                dialect = (AbstractHelperDialect) sqlDialectClass.newInstance();
                dialect.setProperties(properties);
                return dialect;
            }
            throw new PageException("使用 PageHelper 时，方言必须是实现 " + AbstractHelperDialect.class.getCanonicalName() + " 接口的实现类!");
        }
        catch (Exception e)
        {
            throw new PageException("初始化 helper [" + dialectClass + "]时出错:" + e.getMessage(), e);
        }

    }

    /**
     * 反射类
     *
     * @param className
     * @return
     * @throws Exception
     */
    private Class resloveDialectClass(String className) throws Exception
    {
        if (dialectAliasMap.containsKey(className.toLowerCase()))
        {
            return dialectAliasMap.get(className.toLowerCase());
        }
        else
        {
            return Class.forName(className);
        }
    }

    public void setProperties(Properties properties)
    {
        String dialectAlias = properties.getProperty("dialectAlias");
        if (StringUtil.isNotEmpty(dialectAlias))
        {
            String[] alias = dialectAlias.split(";");
            for (int i = 0; i < alias.length; i++)
            {
                String[] kv = alias[i].split("=");
                if (kv.length != 2)
                {
                    throw new IllegalArgumentException("dialectAlias 参数配置错误，" + "请按照 alias1=xx.dialectClass;alias2=dialectClass2 的形式进行配置!");
                }
                for (int j = 0; j < kv.length; j++)
                {
                    try
                    {
                        Class<? extends Dialect> diallectClass = (Class<? extends Dialect>) Class.forName(kv[1]);
                        //允许覆盖已有的实现
                        registerDialectAlias(kv[0], diallectClass);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new IllegalArgumentException("请确保 dialectAlias 配置的 Dialect 实现类存在!", e);
                    }
                }
            }
        }
        //指定的 Helper 数据库方言，和  不同
        String dialect = properties.getProperty("helperDialect");
        //运行时获取数据源
        String runtimeDialect = properties.getProperty("autoRuntimeDialect");
        //1.动态多数据源
        if (StringUtil.isNotEmpty(runtimeDialect) && "TRUE".equalsIgnoreCase(runtimeDialect))
        {
            this.autoDialect = false;
            this.properties = properties;
        }
        //2.动态获取方言
        else if (StringUtil.isEmpty(dialect))
        {
            autoDialect = true;
            this.properties = properties;
        }
        //3.指定方言
        else
        {
            autoDialect = false;
            this.delegate = this.initDialect(dialect, properties);
        }
    }
}
