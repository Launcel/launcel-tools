//package xyz.launcel.session.web.annotation;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.session.data.redis.RedisFlushMode;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.Retention;
//import java.lang.annotation.Target;
//
//@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
//@Target({java.lang.annotation.ElementType.TYPE})
//@Documented
//@Import(PrimyRedisHttpSessionConfiguration.class)
//@Configuration
//public @interface PrimyEnableRedisHttpSession
//{
//
//    int maxInactiveIntervalInSeconds() default 1200;
//
//    String redisNamespace() default "";
//
//    RedisFlushMode redisFlushMode() default RedisFlushMode.ON_SAVE;
//}
