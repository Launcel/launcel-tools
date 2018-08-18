package xyz.launcel.interceptor;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import xyz.launcel.lang.SQLHelp;
import xyz.launcel.log.RootLogger;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Launcel in 2017/10/24
 */
@Deprecated
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class ResultSetInterceptor implements Interceptor, Serializable
{
    private static final long serialVersionUID = -8563836654060071116L;

    private static String[] superMethod = {"selectKey", "select", "queryPaging", "list"};

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        @SuppressWarnings("unused")
        Statement        stmt   = (Statement) invocation.getArgs()[0];
        ResultSetHandler target = (ResultSetHandler) invocation.getTarget();
        //利用反射获取到DefaultResultSetHandler的ParameterHandler属性，从而获取到ParameterObject
        MetaObject      metaObject      = SystemMetaObject.forObject(target);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        String          selectId        = mappedStatement.getId();
        String[]        splitString     = selectId.split("\\.");
        selectId = splitString[splitString.length - 1];
        Class<?> clazz;
        if (Arrays.asList(superMethod).contains(selectId))
        {
            RootLogger.debug("调用 BaseRepository");
            BoundSql            boundSql  = (BoundSql) metaObject.getValue("boundSql");
            @SuppressWarnings("unchecked")
            Map<String, Object> parameter = (Map<String, Object>) boundSql.getParameterObject();
            if (parameter == null)
                return invocation.proceed();
            clazz = SQLHelp.getClazz(parameter);
        }
        else
        {
            List<ResultMap> resultMaps = mappedStatement.getResultMaps();
            if (resultMaps.isEmpty())
                return invocation.proceed();
            ResultMap resultMap = resultMaps.get(0);
            clazz = resultMap != null && resultMap.getType() != null ? resultMap.getType() : null;
        }
        return clazz != null;// && ModelClassUtil.getClasses().contains(clazz) ? handleResultSet(stmt, clazz) : invocation.proceed();
    }

    @Override
    public Object plugin(Object target)
    {
        if (target instanceof ResultSetHandler)
            return Plugin.wrap(target, this);
        else
            return target;
    }

    @Override
    public void setProperties(Properties properties)
    {

    }

    @SuppressWarnings("unused")
    private Object handleResultSet(Statement stmt, Class<?> javaClass) throws SQLException
    {
        ResultSet    rs   = null;
        List<Object> list = new ArrayList<>();
        try
        {
            rs = stmt.getResultSet();
            //                if (rs != null && javaClass != null) {
            //                    Table table = TableMapping.one().getTable(javaClass);
            //                    while (rs.next()) {
            //                        Object obj = javaClass.newInstance();
            //                        for (Map.Entry<String, String> entry : table.getColumnField().entrySet()) {
            //                            Field field = obj.getClass().getDeclaredField(entry.getValue());
            //                            field.setAccessible(true);
            //                            Object value = ModelClassUtil.ValidateObject(field.getType(), rs.getObject(entry.getKey()));
            //                            field.set(obj, value);
            //                        }
            //                        list.add(obj);
            //                    }
            //                }
        }
        finally
        {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        return list;
    }

}
