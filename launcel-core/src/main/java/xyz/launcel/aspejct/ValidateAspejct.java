package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import xyz.launcel.annotation.Validate;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.Json;
import xyz.launcel.lang.ValidateUtils;
import xyz.launcel.log.BaseLogger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class ValidateAspejct extends BaseLogger {

    void preparedArgs(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
//        if (log.isDebugEnabled())
        info("调用了：" + signature.getDeclaringTypeName() + "." + method.getName() + " 方法 ：参数 \n" + Json.toJson(joinPoint.getArgs()));
        Parameter[] params = method.getParameters();
        String group = ValidateUtils.getMethodGroupAnnotation(joinPoint.getSignature().getName());
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(Validate.class)) {
                try {
                    Object object = joinPoint.getArgs()[i];
                    ValidateUtils.validateLimit(object, group);
                } catch (ReflectiveOperationException e) {
                    ExceptionFactory.error("_DEFINE_ERROR_CODE_014");
                }
            }
        }

    }

    void doReturn(JoinPoint joinPoint, Object object) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
//        if (log.isDebugEnabled())
        info("调用：" + signature.getDeclaringTypeName() + "." + method.getName() + " 方法结束 ：结果 \n" + Json.toJson(object));
    }
}