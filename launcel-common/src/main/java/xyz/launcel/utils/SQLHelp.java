package xyz.launcel.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import xyz.launcel.annotation.OrderSqlEnum;
import xyz.launcel.bo.PageQuery;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by xuyang in 2017/10/12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLHelp
{

    public static PageQuery getPaging(Map<String, Object> parameter)
    {
        if (parameter != null && parameter.size() > 0)
        {
            PageQuery p = (PageQuery) getParam(parameter, "page");
            if (Objects.nonNull(p))
            {
                return p;
            }
            for (Object value : parameter.values())
            {
                if (value instanceof PageQuery)
                {
                    return (PageQuery) value;
                }
            }
        }
        return new PageQuery();
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
        return parameter.get(param);
    }

    private static String concatGroupBy(Set<String> group)
    {
        var sb = new StringBuilder();
        sb.append(" GROUP BY ");
        int index = 1;
        for (String s : group)
        {
            sb.append(s);
            index++;
            if (index < group.size())
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static String concatOrderBy(LinkedHashMap<String, OrderSqlEnum> orderBy)
    {
        var sb = new StringBuilder();
        sb.append(" ORDER BY ");
        String headColName = CollectionUtils.MapUtils.getHead(orderBy).getKey();
        for (Map.Entry<String, OrderSqlEnum> entry : orderBy.entrySet())
        {
            if (entry.getKey().equals(headColName))
            {
                sb.append(entry.getKey()).append(" ").append(entry.getValue().name());
                continue;
            }
            sb.append(",").append(entry.getKey()).append(" ").append(entry.getValue().name());
        }
        return sb.toString();
    }

    private static String concatLimit(Integer row, Integer offset)
    {
        if (row != null && row > 0)
        {
            var sb = new StringBuilder(" LIMIT ");
            if (offset != null && offset > 0)
            {
                return sb.append(offset).append(",").append(row).toString();
            }
            return sb.append(row).toString();
        }
        return null;
    }


    public static String concatSql(String boundSql, PageQuery p)
    {
        var sb = new StringBuilder(boundSql);
        if (CollectionUtils.isNotEmpty(p.getGroupBy()))
        {
            sb.append(concatGroupBy(p.getGroupBy()));
        }

        if (CollectionUtils.isNotEmpty(p.getOrderBy()))
        {
            sb.append(concatOrderBy(p.getOrderBy()));
        }

        return sb.append(concatLimit(p.getRow(), p.getOffset())).toString();
    }
}
