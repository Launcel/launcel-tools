package xyz.x.redis.support.lock;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import xyz.x.common.ensure.Predict;
import xyz.x.redis.support.RedisUtils;

import java.lang.reflect.Method;

@Aspect
@Order(-100)
public class RedisLockAspect
{
    private ThreadLocal<RedisLockLocal> localThreadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(xyz.launcel.redis.support.lock.RedisLock)")
    public void init()
    {
        if (localThreadLocal.get().isHasLock())
        {
            long now = System.nanoTime();

            while (localThreadLocal.get().getTime() > now - localThreadLocal.get().getStartTime())
            {
                if (lock())
                {
                    return;
                }
                now = System.nanoTime();
            }
        }
    }

    @Before("init()")
    public void prepared(JoinPoint joinPoint)
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();
        if (method.isAnnotationPresent(RedisLock.class))
        {
            RedisLock redisLock = method.getAnnotation(RedisLock.class);
            if (redisLock != null)
            {
                Predict.builder(redisLock).isNull("Lock 加锁注解不存在");
                Predict.builder(redisLock.key()).isBlank("Lock 加锁的body不存在");
                Predict.builder(redisLock.time()).ltOrEq(0L).isTrue("Lock 加锁的失效时间不能设置为永久或者不能为0");
                Predict.builder(redisLock.key()).isBlank("加锁的key不存在");
                localThreadLocal.get().setKey(redisLock.key());
                localThreadLocal.get().setTime(redisLock.time() * 1000000);
                localThreadLocal.get().setStartTime(System.nanoTime());
                localThreadLocal.get().setHasLock(true);
            }
        }
    }

    @AfterReturning(pointcut = "init()")
    public void after()
    {
        if (localThreadLocal.get().isHasLock())
        { RedisUtils.unclock(localThreadLocal.get().getKey()); }
    }

    private boolean lock()
    {
        if (localThreadLocal.get().isHasLock())
        {
            return RedisUtils.setNxNANO(localThreadLocal.get().getKey(), "1", localThreadLocal.get().getTime());
        }
        return false;
    }
}
