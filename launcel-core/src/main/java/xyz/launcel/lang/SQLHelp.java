package xyz.launcel.lang;

import xyz.launcel.dao.Paging;

import java.util.Map;

/**
 * Created by xuyang in 2017/10/12
 */
public class SQLHelp
{

    private SQLHelp()
    {
    }

    @SuppressWarnings("rawtypes")
    public static Paging getPaging(Map<String, Object> parameter)
    {
        Paging p = null;
        Object o;
        if (parameter.containsKey("page"))
        {
            o = getParam(parameter, "page");
        }
        else
        {
            o = getParam(parameter, "param1");
        }
        if (o instanceof Paging)
        {
            p = (Paging) o;
        }
        return p == null ? new Paging(15) : p;
    }

    public static Class<?> getClazz(Map<String, Object> parameter)
    {
        Class<?> clazz = null;
        Object   o     = getParam(parameter, "clazz");
        if (o instanceof Class)
        {
            clazz = (Class<?>) o;
        }
        return clazz;
    }

    private static Object getParam(Map<String, Object> parameter, String param)
    {
        return parameter != null ? (parameter.isEmpty() ? null : parameter.get(param)) : null;
    }

    public static String concatSql(String boundSql, Paging<?> p)
    {
        StringBuilder sb = new StringBuilder(boundSql);
        sb.append(" LIMIT ");
        if (p.getOffset() > 0)
        {
            sb.append(p.getOffset()).append(",").append(p.getMaxRow());
        }
        else
        {
            sb.append(p.getMaxRow());
        }
        return sb.toString();
    }
}
