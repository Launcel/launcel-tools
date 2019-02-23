package xyz.launcel.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import xyz.launcel.annotation.OrderSqlEnum;
import xyz.launcel.dao.Page;
import xyz.launcel.exception.SystemException;

import java.util.Map;

/**
 * Created by xuyang in 2017/10/12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLHelp
{

    public static Page getPaging(Map<String, Object> parameter)
    {
        Page p = null;
        try
        {
            if (parameter.containsKey("page"))
            {
                p = (Page) getParam(parameter, "page");
            }
            else
            {
                for (var value : parameter.values())
                {
                    if (value instanceof Page)
                    {
                        p = (Page) value;
                        break;
                    }
                }
            }
        }
        catch (ClassCastException x)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "page对象转换出错");
        }
        return p == null ? new Page(Integer.MAX_VALUE) : p;
    }

    public static Class<?> getClazz(Map<String, Object> parameter)
    {
        Class<?> clazz = null;
        var      o     = getParam(parameter, "clazz");
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

    public static String concatSql(String boundSql, Page<?> p)
    {
        var sb = new StringBuilder(boundSql);
        if (CollectionUtils.isNotEmpty(p.getGroupBy()))
        {
            sb.append(" GROUP BY ");
            boolean isTail = true;
            for (String s : p.getGroupBy())
            {
                if (!isTail)
                {
                    sb.append(",").append(s);
                    continue;
                }
                sb.append(s);
                isTail = false;
            }
        }

        if (CollectionUtils.isNotEmpty(p.getOrderBy()))
        {
            sb.append(" ORDER BY ");
            String headColName = CollectionUtils.getHead(p.getOrderBy()).getKey();

            for (Map.Entry<String, OrderSqlEnum> entry : p.getOrderBy().entrySet())
            {
                if (entry.getKey().equals(headColName))
                {
                    sb.append(entry.getKey()).append(" ").append(entry.getValue().name());
                    continue;
                }
                sb.append(",").append(entry.getKey()).append(" ").append(entry.getValue().name());
            }
        }

        if (p.getRow() != null && p.getRow() > 0)
        {
            sb.append(" LIMIT ");

            if (p.getOffset() > 0)
            {
                return sb.append(p.getOffset()).append(",").append(p.getRow()).toString();
            }
            return sb.append(p.getRow()).toString();
        }
        return sb.toString();
    }
}
