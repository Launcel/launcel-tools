package xyz.launcel.redis.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebTokenProperties
{

    private static String tokenKey;

    @Value("${web.token-key:{null}}")
    public void setTokenKey(String tokenKey) {
        WebTokenProperties.tokenKey = tokenKey;
    }

    public static String getTokenKey() {
        return tokenKey;
    }
}
