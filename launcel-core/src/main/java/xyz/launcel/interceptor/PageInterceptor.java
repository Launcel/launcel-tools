package xyz.launcel.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import xyz.launcel.dao.Paging;
import xyz.launcel.lang.SQLHelp;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.BaseLogger;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Launcel in 2017/10/12
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor extends BaseLogger implements Interceptor, Serializable
{
    private static final long serialVersionUID = 3637036555137206361L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        StatementHandler statementHandler     = (StatementHandler) invocation.getTarget();
        MetaObject       metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement  mappedStatement      = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        String           selectId             = mappedStatement.getId();
        if (selectId.matches(".*Page.*"))
        {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            // 分页参数作为参数对象 parameter 的一个属性
            String sql = boundSql.getSql();
            if (StringUtils.isBlank(sql))
            {
                return invocation.proceed();
            }
            @SuppressWarnings("unchecked") Map<String, Object> parameter = (Map<String, Object>) boundSql.getParameterObject();
            if (parameter.isEmpty())
            {
                return invocation.proceed();
            }
            Paging<?> p       = SQLHelp.getPaging(parameter);
            String    pageSql = SQLHelp.concatSql(sql, p);
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o)
    {
        if (o instanceof StatementHandler)
        {
            return Plugin.wrap(o, this);
        }
        else
        {
            return o;
        }
    }

    @Override
    public void setProperties(Properties properties) { }

}
