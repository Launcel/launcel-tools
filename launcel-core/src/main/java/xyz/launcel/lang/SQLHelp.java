package xyz.launcel.lang;

import xyz.launcel.dao.Page;
import xyz.launcel.exception.SystemException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuyang in 2017/10/12
 */
public class SQLHelp
{

    private SQLHelp() { }

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
                for (Object value : parameter.values())
                {
                    if (value instanceof Page)
                    {
                        p = (Page) value;
                        break;
                    }
                }
            }
        }
        catch (ClassCastException x) { throw new SystemException("_DEFINE_ERROR_CODE_011", "page对象转换出错"); }
        return p == null ? new Page(Integer.MAX_VALUE) : p;
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

    public static String concatSql(String boundSql, Page<?> p)
    {
        StringBuilder sb = new StringBuilder(boundSql);
        if (CollectionUtils.isNotEmpty(p.getGroupBy()))
        {
            sb.append(" GROUP BY ");
            Set<Integer> indexSet = new HashSet<>(1);
            indexSet.add(1);
            p.getGroupBy().forEach(groupSet -> {
                if (indexSet.contains(1))
                {
                    sb.append(groupSet);
                    indexSet.clear();
                }
                else { sb.append(",").append(groupSet); }
            });
        }
        if (CollectionUtils.isNotEmpty(p.getOrderBy()))
        {
            sb.append(" ORDER BY ");
            String headColName = CollectionUtils.getHead(p.getOrderBy()).getKey();
            p.getOrderBy().forEach((colName, order) -> {
                if (colName.equals(headColName))
                {
                    sb.append(colName).append(" ").append(order.name());
                }
                else
                {
                    sb.append(",").append(colName).append(" ").append(order.name());
                }
            });
        }

        sb.append(" LIMIT ");

        if (p.getOffset() > 0)
        {
            sb.append(p.getOffset()).append(",").append(p.getRow());
        }
        else
        {
            sb.append(p.getRow());
        }

        return sb.toString();
    }
}
