package xyz.x.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import xyz.x.bo.PageQuery;

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
        if (CollectionUtils.isEmpty(group))
        {
            return "";
        }
        var sb = new StringBuilder();
        sb.append(" GROUP BY ");
        for (String s : group)
        {
            sb.append(s).append(",");
        }
        String groupBy = sb.toString();
        return groupBy.substring(0, groupBy.length() - 1);
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
        return "";
    }


    public static String concatSql(String boundSql, PageQuery p)
    {
        var sb = new StringBuilder(boundSql).append(p.getGroupString()).append(p.getOrderString());
        return sb.append(concatLimit(p.getPageNum(), p.getOffset())).toString();
    }
}
