package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import xyz.launcel.annotation.AnnotationConston;
import xyz.launcel.annotation.DataSource;
import xyz.launcel.holder.DbContextHolder;
import xyz.launcel.lang.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by launcel on 2018/8/29.
 */
@Aspect
@Order(-100)
public class DataSourceSwitchAspect
{
    @Pointcut(AnnotationConston.dataSource)
    public void init() { }

    @Before("init()")
    public void prepared(JoinPoint joinPoint)
    {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method    = signature.getMethod();
        if (method.isAnnotationPresent(DataSource.class))
        {
            var ds = method.getAnnotation(DataSource.class);
            if (ds != null && StringUtils.isNotBlank(ds.name()))
            {
                DbContextHolder.setDbType(ds.name());
            }
        }
    }

    @AfterReturning(pointcut = "init()")
    public void after()
    {
        DbContextHolder.clearDbType();
    }
}
