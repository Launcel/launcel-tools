package xyz.launcel.jdbc.interceptor;

import com.github.pagehelper.Page;
import org.apache.ibatis.cache.CacheKey;

public class MySqlDialect extends com.github.pagehelper.dialect.helper.MySqlDialect
{

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey)
    {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        if (page.getStartRow() == 0)
        {
            sqlBuilder.append(" LIMIT ? ");
        }
        else
        {
            sqlBuilder.append(" LIMIT ?, ? ");
        }
        return sqlBuilder.toString();
    }
}
