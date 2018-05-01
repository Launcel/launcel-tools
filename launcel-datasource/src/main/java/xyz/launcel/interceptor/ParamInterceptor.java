package xyz.launcel.interceptor;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import xyz.launcel.lang.StringUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({ @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class) })
public class ParamInterceptor implements Interceptor {
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        
        long             startTime        = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) target;
        try {
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long sqlCost = endTime - startTime;
            
            BoundSql               boundSql             = statementHandler.getBoundSql();
            String                 sql                  = boundSql.getSql();
            Object                 parameterObject      = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
            
            // 格式化Sql语句，去除换行符，替换参数
            sql = formatSql(sql, parameterObject, parameterMappingList);
            if (StringUtils.isNotBlank(sql)) {
                System.out.println("=============================================================\n");
                System.out.println("SQL：[" + sql + "]执行耗时[" + sqlCost + "ms]");
                System.out.println("=============================================================");
            }
        }
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
    
    }
    
    @SuppressWarnings("unchecked")
    private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
        // 输入sql字符串空判断
        if (sql == null || sql.length() == 0) {
            return "";
        }
        
        // 美化sql
        sql = beautifySql(sql);
        
        // 不传参数的场景，直接把Sql美化一下返回出去
        if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
            return sql;
        }
        
        // 定义一个没有替换过占位符的sql，用于出异常时返回
        String sqlWithoutReplacePlaceholder = sql;
        
        try {
            Class<?> parameterObjectClass = parameterObject.getClass();
            
            // 如果参数是StrictMap且Value类型为Collection，获取key="list"的属性，这里主要是为了处理<foreach>循环时传入List这种参数的占位符替换
            // 例如select * from xxx where id in <foreach collection="list">...</foreach>
            if (isStrictMap(parameterObjectClass)) {
                StrictMap<Collection<?>> strictMap = (StrictMap<Collection<?>>) parameterObject;
                
                if (isList(strictMap.get("list").getClass())) {
                    sql = handleListParameter(sql, strictMap.get("list"));
                }
            } else if (isMap(parameterObjectClass)) {
                // 如果参数是Map则直接强转，通过map.get(key)方法获取真正的属性值
                // 这里主要是为了处理<insert>、<delete>、<update>、<select>时传入parameterType为map的场景
                Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
                sql = handleMapParameter(sql, paramMap, parameterMappingList);
            } else {
                // 通用场景，比如传的是一个自定义的对象或者八种基本数据类型之一或者String
                sql = handleCommonParameter(sql, parameterMappingList, parameterObjectClass, parameterObject);
            }
        } catch (Exception e) {
            // 占位符替换过程中出现异常，则返回没有替换过占位符但是格式美化过的sql，这样至少保证sql语句比BoundSql中的sql更好看
            return sqlWithoutReplacePlaceholder;
        }
        return sql;
    }
    
    /**
     * 美化Sql
     */
    private String beautifySql(String sql) {
        sql = sql.replace("\n", "").replace("\t", "").replace("  ", " ").replace("( ", "(").replace(" )", ")").replace(" ,", ",");
        
        return sql;
    }
    
    /**
     * 处理参数为List的场景
     */
    private String handleListParameter(String sql, Collection<?> col) {
        if (col != null && col.size() != 0) {
            for (Object obj : col) {
                String   value    = null;
                Class<?> objClass = obj.getClass();
                
                // 只处理基本数据类型、基本数据类型的包装类、String这三种
                // 如果是复合类型也是可以的，不过复杂点且这种场景较少，写代码的时候要判断一下要拿到的是复合类型中的哪个属性
                if (isPrimitiveOrPrimitiveWrapper(objClass)) {
                    value = obj.toString();
                } else if (objClass.isAssignableFrom(String.class)) {
                    value = "\"" + obj.toString() + "\"";
                }
                if (StringUtils.isNotBlank(value)) {
                    sql = sql.replaceFirst("\\?", value);
                }
            }
        }
        
        return sql;
    }
    
    /**
     * 处理参数为Map的场景
     */
    private String handleMapParameter(String sql, Map<?, ?> paramMap, List<ParameterMapping> parameterMappingList) {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            Object propertyName  = parameterMapping.getProperty();
            Object propertyValue = paramMap.get(propertyName);
            if (propertyValue != null) {
                if (propertyValue.getClass().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
                
                sql = sql.replaceFirst("\\?", propertyValue.toString());
            }
        }
        
        return sql;
    }
    
    /**
     * 处理通用的场景
     */
    private String handleCommonParameter(String sql, List<ParameterMapping> parameterMappingList, Class<?> parameterObjectClass,
                                         Object parameterObject) throws Exception {
        for (ParameterMapping parameterMapping : parameterMappingList) {
            String propertyValue;
            // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取paramterMapping中的property属性即可
            if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
                propertyValue = parameterObject.toString();
            } else {
                String propertyName = parameterMapping.getProperty();
                
                Field field = parameterObjectClass.getDeclaredField(propertyName);
                // 要获取Field中的属性值，这里必须将私有属性的accessible设置为true
                field.setAccessible(true);
                propertyValue = String.valueOf(field.get(parameterObject));
                if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
                    propertyValue = "\"" + propertyValue + "\"";
                }
            }
            
            sql = sql.replaceFirst("\\?", propertyValue);
        }
        
        return sql;
    }
    
    /**
     * 是否基本数据类型或者基本数据类型的包装类
     */
    private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
        return parameterObjectClass.isPrimitive() ||
                (parameterObjectClass.isAssignableFrom(Byte.class) || parameterObjectClass.isAssignableFrom(Short.class) ||
                        parameterObjectClass.isAssignableFrom(Integer.class) || parameterObjectClass.isAssignableFrom(Long.class) ||
                        parameterObjectClass.isAssignableFrom(Double.class) || parameterObjectClass.isAssignableFrom(Float.class) ||
                        parameterObjectClass.isAssignableFrom(Character.class) || parameterObjectClass.isAssignableFrom(Boolean.class));
    }
    
    /**
     * 是否DefaultSqlSession的内部类StrictMap
     */
    private boolean isStrictMap(Class<?> parameterObjectClass) {
        return parameterObjectClass.isAssignableFrom(StrictMap.class);
    }
    
    /**
     * 是否List的实现类
     */
    private boolean isList(Class<?> clazz) {
        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(List.class)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 是否Map的实现类
     */
    private boolean isMap(Class<?> parameterObjectClass) {
        Class<?>[] interfaceClasses = parameterObjectClass.getInterfaces();
        for (Class<?> interfaceClass : interfaceClasses) {
            if (interfaceClass.isAssignableFrom(Map.class)) {
                return true;
            }
        }
        
        return false;
    }
    
    
    protected static class StrictMap<V> extends HashMap<String, V> {
        private static final long   serialVersionUID = -4950446264854982944L;
        private final        String name;
        
        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }
        
        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }
        
        public StrictMap(String name) {
            this.name = name;
        }
        
        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }
        
        public V put(String key, V value) {
            if (this.containsKey(key)) {
                throw new IllegalArgumentException(this.name + " already contains value for " + key);
            } else {
                if (key.contains(".")) {
                    String shortKey = this.getShortName(key);
                    if (get(shortKey) == null) {
                        put(shortKey, value);
                    } else {
                        //noinspection unchecked
                        Ambiguity<V> ambiguity = new Ambiguity(shortKey);
                        put(shortKey, ambiguity.getSubject());
                    }
                }
                
                return put(key, value);
            }
        }
        
        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(this.name + " does not contain value for " + key);
            } else {
                return value;
            }
        }
        
        private String getShortName(String key) {
            String[] keyParts = key.split("\\.");
            return keyParts[keyParts.length - 1];
        }
        
        protected static class Ambiguity<V> {
            private final V subject;
            
            public Ambiguity(V subject) {
                this.subject = subject;
            }
            
            public V getSubject() {
                return this.subject;
            }
        }
    }
    
}