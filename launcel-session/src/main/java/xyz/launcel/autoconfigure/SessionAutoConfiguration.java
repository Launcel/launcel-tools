//package xyz.launcel.autoconfigure;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
//import xyz.launcel.session.web.annotation.PrimyEnableRedisHttpSession;
//
//@Configuration
//@PrimyEnableRedisHttpSession
//public class SessionAutoConfiguration
//{
//    @Primary
//    @Bean
//    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver()
//    {
//        return new HeaderHttpSessionIdResolver("x-auth-token");
//    }
//
//}
//
