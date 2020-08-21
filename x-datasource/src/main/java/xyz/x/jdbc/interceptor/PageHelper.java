package xyz.x.jdbc.interceptor;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageParams;
import com.github.pagehelper.parser.CountSqlParser;
import com.github.pagehelper.util.MSUtils;
import com.github.pagehelper.util.StringUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

public class PageHelper extends com.github.pagehelper.PageHelper
{
    private PageParams      pageParams;
    private PageAutoDialect autoDialect;

    @Override
    public boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds)
    {
        if (ms.getId().endsWith(MSUtils.COUNT))
        {
            throw new RuntimeException("在系统中发现了多个分页插件，请检查系统配置!");
        }
        Page page = pageParams.getPage(parameterObject, rowBounds);
        if (page == null)
        {
            return true;
        }
        else
        {
            //设置默认的 count 列
            if (StringUtil.isEmpty(page.getCountColumn()))
            {
                page.setCountColumn(pageParams.getCountColumn());
            }
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public void setProperties(Properties properties)
    {
        setStaticProperties(properties);
        pageParams = new PageParams();
        autoDialect = new PageAutoDialect();
        pageParams.setProperties(properties);
        autoDialect.setProperties(properties);
        //20180902新增 aggregateFunctions, 允许手动添加聚合函数（影响行数）
        CountSqlParser.addAggregateFunctions(properties.getProperty("aggregateFunctions"));
    }
}
