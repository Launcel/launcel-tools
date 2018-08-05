/*
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.12.12
 * Version: 1.0
 */

package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "db.redis")
public class RedisProperties
{
    private String  host      = "127.0.0.1";
    private String  password  = "123456";
    private Integer port      = 6379;
    private Integer database  = 0;
    private Integer minIdle   = 0;
    // 可用连接实例的最大数目，默认值为8；
    private Integer maxIdle   = 8;
    private Integer maxTotal  = 10;
    private Integer maxActive = 10;
    private Long    maxWait   = 30000L;
    private Long    exptime   = 600L;
    private Integer timeout   = 300;

}
