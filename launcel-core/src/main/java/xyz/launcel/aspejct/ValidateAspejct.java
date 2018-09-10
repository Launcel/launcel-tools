package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.launcel.annotation.Validate;
import xyz.launcel.exception.SystemException;
import xyz.launcel.json.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.lang.ValidateUtils;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author launcel
 */
class ValidateAspejct
{

    void preparedArgs(JoinPoint joinPoint)
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (RootLogger.isDebug())
        {
            if (method.getName().toLowerCase().contains("upload"))
            {
                RootLogger.debug("调用了：{}.upload* 方法,可能是上传文件,不输出参数,如有误,请重命名该方法,不要包含upload关键字", signature.getDeclaringTypeName());
                return;
            }
            String   params = "";
            Object[] temp   = joinPoint.getArgs();
            if (Objects.nonNull(temp) && temp.length > 0)
            { params = Json.toJson(temp); }
            RootLogger.debug("调用了：{}.{} 方法 ：参数 \n{}", signature.getDeclaringTypeName(), method.getName(), params);
        }
        Parameter[] params = method.getParameters();
        String      group  = StringUtils.capitalize(joinPoint.getSignature().getName());

        for (int i = 0; i < params.length; i++)
        {
            if (params[i].isAnnotationPresent(Validate.class))
            {
                Object object = joinPoint.getArgs()[i];
                ValidateUtils.validateLimit(object, group);
            }
        }
    }

    void doReturn(JoinPoint joinPoint, Object object)
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (RootLogger.isDebug())
        {
            String returns = "";
            if (Objects.nonNull(object))
            { returns = Json.toJson(object); }
            RootLogger.debug("调用了：{}.{} 方法结束 ：结果 \n{}", signature.getDeclaringTypeName(), method.getName(), returns);
        }
    }
}