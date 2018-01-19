package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ControllerParamValidateAspejct extends ValidateAspejct {


    private final String point = "execution(public * team.uncle.controller.*.*(..))";
//    private final String point = "@annotation(xyz.launcel.annotation.Validate)";

    @Pointcut(point)
    public void init() {
    }

    @Before("init()")
    public void prepared(JoinPoint joinPoint) {
        preparedArgs(joinPoint);
    }

    @AfterReturning(pointcut = "init()", returning = "object")
    public void after(JoinPoint joinPoint, Object object) {
        doReturn(joinPoint, object);
    }

}