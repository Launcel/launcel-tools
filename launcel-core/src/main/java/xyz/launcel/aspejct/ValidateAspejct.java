package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.launcel.annotation.Validate;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.json.Json;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.lang.ValidateUtils;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class ValidateAspejct {
    
    void preparedArgs(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (method.getName().toLowerCase().contains("upload")) {
            return;
        }
        if (RootLogger.isDebug()) {
            RootLogger.debug("调用了：" + signature.getDeclaringTypeName() + "." + method.getName() + " 方法 ：参数 \n" + Json.toJson(joinPoint.getArgs()));
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
        if (RootLogger.isDebug()) {
            RootLogger.debug("调用：" + signature.getDeclaringTypeName() + "." + method.getName() + " 方法结束 ：结果 \n" + Json.toJson(object));
        }
    }
}