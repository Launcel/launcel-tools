package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;
import xyz.launcel.annotation.AnnotationConston;
import xyz.launcel.annotation.Limit;
import xyz.launcel.annotation.ParamValidate;
import xyz.launcel.annotation.Types;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.RegUtil;
import xyz.launcel.log.RootLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * Created by xuyang in 2017/10/11
 */
//@Aspect
//@Component
@Deprecated
public class ParamValidatorAspect
{

    @Pointcut(AnnotationConston.paramValidatorPoint)
    public void initValidate() { }

    @Before("initValidate()")
    public void prepardArgs(JoinPoint joinPoint)
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        // 获得方法的参数
        Parameter[] params = method.getParameters();
        /**
         * 获得当前方法中的参数注解（ps：代理对象获取不到即便是注解可被继承，只能获取到原始对象上的注解，所以代理对象必需同时在原对象添加此注解）
         */
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (null == parameterAnnotations || parameterAnnotations.length <= 0)
            return;
        int i = 0;
        // 循环每个参数上的注解
        for (Annotation[] parameterAnnotation : parameterAnnotations)
        {
            // 循环当前参数上的注解
            for (Annotation annotation : parameterAnnotation)
            {
                if (annotation instanceof ParamValidate)
                {
                    ParamValidate temp = (ParamValidate) annotation;
                    try
                    {
                        verifyGroup(params[i], temp.group());
                    }
                    catch (IllegalAccessException e)
                    {
                        ExceptionFactory.create(e.getMessage());
                    }
                    catch (InstantiationException e)
                    {
                        RootLogger.error("不可能的错误，{} 实例化错误", params[i].getType().getName());
                    }
                }
            }
            i++;
        }
    }

    private void verifyGroup(Parameter p, Class<?> group) throws InstantiationException, IllegalAccessException
    {
        Class<?> clazz  = p.getType();
        Field[]  fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            verifyLimit(field, clazz.newInstance(), group);
            field.setAccessible(false);
        }
    }

    private void verifyLimit(Field f, Object o, Class<?> group) throws IllegalAccessException
    {
        //查看是否具有指定类型的注释：
        if (!f.isAnnotationPresent(Limit.class))
            return;
        Limit l = f.getAnnotation(Limit.class);
        if (l.group().length > 0)
        {
            for (Class<?> aClass : l.group())
            {
                if (aClass == group)
                    //if (l.notBlank())
                    //{
                    checkFiled(f, o, l);
                //}
            }
        }
        // 没有 group ，则默认全局验证
        else
        {
            checkFiled(f, o, l);
        }
    }

    private void checkFiled(Field f, Object o, Limit l) throws IllegalAccessException
    {
        Object value   = f.get(o);
        String message = l.message();
        verifyBlank(value, message);
        Types tc = l.type();
        verifyType(value, message, tc);
    }


    private void verifyBlank(Object o, String message) { Assert.isTrue(Objects.nonNull(o), message); }

    private void verifyType(Object o, String message, Types tc)
    {
        if (tc == Types.string)
        {
            Assert.isTrue(Objects.nonNull(o), message);
        }
        else if (tc == Types.number)
        {
            Assert.isTrue(RegUtil.isNum(o.toString()), message);
        }
        else if (tc == Types.ip)
        {
            Assert.isTrue(RegUtil.isIP(o.toString()), message);
        }
        else if (tc == Types.decimal)
        {
            Assert.isTrue(RegUtil.isFloatNum(o.toString()), message);
        }
        else if (tc == Types.email)
        {
            Assert.isTrue(RegUtil.isEmail(o.toString()), message);
        }
        else if (tc == Types.tel)
        {
            Assert.isTrue(RegUtil.isMobile(o.toString()), message);
        }
        else if (tc == Types.qq)
        {
            Assert.isTrue(RegUtil.isQQ(o.toString()), message);
        }
    }
}
