package xyz.launcel.redis.support.lock;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock
{
    String key();

    long time(); // 毫秒级别

    TimeUnit unit() default TimeUnit.NANO;
}
