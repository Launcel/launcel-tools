package xyz.launcel.aspejct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import xyz.launcel.annotation.AnnotationConston;

@Aspect
public class ControllerParamValidateAspejct extends ValidateAspejct
{


//    private final String point = "execution(public * xyz.launcel..*.controller.*.*(..))";

    @Pointcut(AnnotationConston.mapping)
    public void init() { }

    @Before("init()")
    public void prepared(JoinPoint joinPoint)
    {
        preparedArgs(joinPoint);
    }

    @AfterReturning(pointcut = "init()", returning = "object")
    public void after(JoinPoint joinPoint, Object object)
    {
        doReturn(joinPoint, object);
    }

}
