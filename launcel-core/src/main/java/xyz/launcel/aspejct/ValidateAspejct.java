package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.launcel.annotation.Validate;
import xyz.launcel.json.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.lang.ValidateUtils;
import xyz.launcel.log.RootLogger;

import java.util.Objects;

/**
 * @author launcel
 */
class ValidateAspejct
{

    void preparedArgs(JoinPoint joinPoint)
    {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method    = signature.getMethod();
        if (RootLogger.isDebug())
        {
            if (method.getName().toLowerCase().contains("upload"))
            {
                RootLogger.debug("调用了：{}.upload* 方法,可能是上传文件,不输出参数,如有误,请重命名该方法,不要包含upload关键字", signature.getDeclaringTypeName());
                return;
            }
            var params = "";
            var temp   = joinPoint.getArgs();
            if (Objects.nonNull(temp) && temp.length > 0)
            { params = Json.toString(temp); }
            RootLogger.debug("调用了：{}.{} 方法 ：参数 \n{}", signature.getDeclaringTypeName(), method.getName(), params);
        }
        var params = method.getParameters();
        var group  = StringUtils.capitalize(joinPoint.getSignature().getName());

        for (int i = 0; i < params.length; i++)
        {
            if (params[i].isAnnotationPresent(Validate.class))
            {
                var object = joinPoint.getArgs()[i];
                ValidateUtils.validateLimit(object, group);
            }
        }
    }

    void doReturn(JoinPoint joinPoint, Object object)
    {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method    = signature.getMethod();
        if (RootLogger.isDebug())
        {
            var returns = "";
            if (Objects.nonNull(object))
            { returns = Json.toString(object); }
            RootLogger.debug("调用了：{}.{} 方法结束 ：结果 \n{}", signature.getDeclaringTypeName(), method.getName(), returns);
        }
    }
}