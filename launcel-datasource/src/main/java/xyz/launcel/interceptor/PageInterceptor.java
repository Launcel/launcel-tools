package xyz.launcel.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import xyz.launcel.utils.SQLHelp;
import xyz.launcel.utils.StringUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Launcel in 2017/10/12
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor, Serializable
{
    private static final long serialVersionUID = 3637036555137206361L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        var      statementHandler     = (StatementHandler) invocation.getTarget();
        var      metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        var      mappedStatement      = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        var      selectId             = mappedStatement.getId();
        BoundSql boundSql;
        if (selectId.matches(".*Page.*"))
        {
            boundSql = statementHandler.getBoundSql();
            // 分页参数作为参数对象 parameter 的一个属性
            var sql = boundSql.getSql();
            if (StringUtils.isBlank(sql))
            {
                return invocation.proceed();
            }
            @SuppressWarnings("unchecked")
            var parameter = (Map<String, Object>) boundSql.getParameterObject();
            if (parameter.isEmpty())
            {
                return invocation.proceed();
            }
            var p       = SQLHelp.getPaging(parameter);
            var pageSql = SQLHelp.concatSql(sql, p);
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
        return o;
    }

    @Override
    public void setProperties(Properties properties) {}
}
