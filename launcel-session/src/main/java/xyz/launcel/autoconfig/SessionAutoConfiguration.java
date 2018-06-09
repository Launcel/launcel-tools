package xyz.launcel.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.web.http.HttpSessionStrategy;
import xyz.launcel.session.web.annotation.PrimyEnableRedisHttpSession;
import xyz.launcel.session.web.http.PrimyCookieHttpSessionStrategy;

@Configuration
@PrimyEnableRedisHttpSession
public class SessionAutoConfiguration
{

    @Primary
    @Bean
    public HttpSessionStrategy httpSessionStrategy()
    {
        //        HttpSessionStrategy strategy = new HeaderHttpSessionStrategy();
        return new PrimyCookieHttpSessionStrategy();
    }

}

