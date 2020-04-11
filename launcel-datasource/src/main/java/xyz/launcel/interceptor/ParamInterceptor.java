package xyz.launcel.interceptor;

import lombok.var;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import xyz.launcel.log.Log;
import xyz.launcel.utils.CollectionUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class ParamInterceptor implements Interceptor
{
    // 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
    private static String getSql(Configuration configuration, BoundSql boundSql, String sqlId)
    {
        return sqlId + ":" + showSql(configuration, boundSql);
    }

    /**
     * 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理<br>
     */

    private static String getParameterValue(Object obj)
    {
        if (obj instanceof String)
        {
            return "'" + obj.toString() + "'";
        }
        if (obj instanceof Date)
        {
            var formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + formatter.format(new Date()) + "'";
        }
        return obj != null ? obj.toString() : "";
    }

    private static String showSql(Configuration configuration, BoundSql boundSql)
    {
        var parameterObject   = boundSql.getParameterObject();
        var parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        var sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null)
        {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            var typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()))
            {
                return sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            }
            // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,
            // 主要支持对JavaBean、Collection、Map三种类型对象的操作
            var metaObject = configuration.newMetaObject(parameterObject);
            for (var parameterMapping : parameterMappings)
            {
                var propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName))
                {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(metaObject.getValue(propertyName))));
                    continue;
                }
                if (boundSql.hasAdditionalParameter(propertyName))
                {
                    // 该分支是动态sql
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(boundSql.getAdditionalParameter(propertyName))));
                    continue;
                }
                sql = sql.replaceFirst("\\?", "缺失");
            }
        }
        return sql;
    }
    // 进行？的替换

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        // 获取xml中的一个select/update/insert/delete节点，主要描述的是一条SQL语句
        var    mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter       = null;
        // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
        if (invocation.getArgs().length > 1)
        {
            parameter = invocation.getArgs()[1];
        }
        // 获取到节点的id,即sql语句的id
        var sqlId = mappedStatement.getId();
        // BoundSql就是封装myBatis最终产生的sql类
        var boundSql = mappedStatement.getBoundSql(parameter);
        // 获取节点的配置
        var configuration = mappedStatement.getConfiguration();
        // 获取到最终的sql语句
        var sql = getSql(configuration, boundSql, sqlId);
        Log.debug("sql = " + sql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}