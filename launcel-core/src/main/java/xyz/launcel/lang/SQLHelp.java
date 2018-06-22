package xyz.launcel.lang;

import xyz.launcel.dao.Paging;
import xyz.launcel.exception.ExceptionFactory;

import java.util.Map;

/**
 * Created by xuyang in 2017/10/12
 */
public class SQLHelp
{

    private SQLHelp() { }

    public static Paging getPaging(Map<String, Object> parameter)
    {
        Paging p = null;
        try
        {
            if (parameter.containsKey("page"))
            {
                p = (Paging) getParam(parameter, "page");
            }
            else
            {
                for (Object value : parameter.values())
                {
                    if (value instanceof Paging)
                    {
                        p = (Paging) value;
                        break;
                    }
                }
            }
        }
        catch (ClassCastException x) { ExceptionFactory.create("_DEFINE_ERROR_CODE_011", "page对象转换出错"); }
        return p == null ? new Paging(Integer.MAX_VALUE) : p;
    }

    public static Class<?> getClazz(Map<String, Object> parameter)
    {
        Class<?> clazz = null;
        Object   o     = getParam(parameter, "clazz");
        if (o instanceof Class) { clazz = (Class<?>) o; }
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
