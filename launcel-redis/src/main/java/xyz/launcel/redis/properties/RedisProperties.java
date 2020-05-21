/*
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.12.12
 * Version: 1.0
 */

package xyz.launcel.redis.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.launcel.common.utils.StringUtils;

@Getter
@Setter
@ConfigurationProperties(prefix = "db.redis")
public class RedisProperties
{
    @NonNull
    private String  host              = "127.0.0.1";
    @NonNull
    private String  password          = "123456";
    private Integer port              = 6379;
    private Integer database          = 0;
    private Integer minIdle           = 0;
    // 可用连接实例的最大数目，默认值为8；
    private Integer maxIdle           = 8;
    private Integer maxTotal          = 10;
    private Integer maxActive         = 10;
    private Long    maxWait           = 30000L;
    private Long    exptime           = 600L;
    @NonNull
    private Integer timeout           = 300;
    private String  valueSerializer   = "org.springframework.data.redis.serializer.JdkSerializationRedisSerializer";
    private String  modelName;
    private String  hashKeySerializer = "org.springframework.data.redis.serializer.StringRedisSerializer";

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
        InnerRedisProperties.prefixKey = this.modelName;
    }

    public static String getPrefixKey()
    {
        if (StringUtils.isBlank(InnerRedisProperties.prefixKey))
        {
            return "";
        }
        if (InnerRedisProperties.prefixKey.endsWith(":"))
        {
            return InnerRedisProperties.prefixKey;
        }
        return InnerRedisProperties.prefixKey.concat(":");
    }

    @Override
    public String toString()
    {
        return "RedisProperties{" + "host='" + host + '\'' + ", password='" + password + '\'' + ", port=" + port + ", database=" + database + ", minIdle=" + minIdle + ", maxIdle=" + maxIdle + ", maxTotal=" + maxTotal + ", maxActive=" + maxActive + ", maxWait=" + maxWait + ", exptime=" + exptime + ", timeout=" + timeout + ", valueSerializer='" + valueSerializer + '\'' + ", modelName='" + modelName + '\'' + ", hashKeySerializer='" + hashKeySerializer + '\'' + '}';
    }

    static class InnerRedisProperties
    {
        private static String prefixKey;
    }
}
