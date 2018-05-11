package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.launcel.annotation.Validate;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.json.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.lang.ValidateUtils;
import xyz.launcel.log.BaseLogger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class ValidateAspejct extends BaseLogger {
    
    void preparedArgs(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (method.getName().toLowerCase().contains("upload")) {
            DEBUG("调用了：{}.upload* 方法,可能是上传文件,不输出参数,如有误,请重命名该方法,不要包含upload关键字", signature.getDeclaringTypeName());
            return;
        }
        if (isDebug()) {
            DEBUG("调用了：{}.{} 方法 ：参数 \n{}", signature.getDeclaringTypeName(), method.getName(), Json.toJson(joinPoint.getArgs()));
        }
        Parameter[] params = method.getParameters();
        String      group  = StringUtils.capitalize(joinPoint.getSignature().getName());
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(Validate.class)) {
                try {
                    Object object = joinPoint.getArgs()[i];
                    ValidateUtils.validateLimit(object, group);
                } catch (ReflectiveOperationException e) {
                    ExceptionFactory.error("_DEFINE_ERROR_CODE_014", "Validate 校验失败");
                }
            }
        }
    }
    
    void doReturn(JoinPoint joinPoint, Object object) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (isDebug()) {
            DEBUG("调用了：{}.{} 方法结束 ：结果 \n{}", signature.getDeclaringTypeName(), method.getName(), Json.toJson(object));
        }
    }
}