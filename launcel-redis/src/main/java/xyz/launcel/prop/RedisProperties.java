/*
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.12.12
 * Version: 1.0
 */

package xyz.launcel.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "db.redis")
public class RedisProperties {
    private String  host      = "127.0.0.1";
    private String  password  = "123456";
    private Integer port      = 6379;
    private Integer database  = 0;
    private Integer minIdle   = 0;
    // 可用连接实例的最大数目，默认值为8；
    private Integer maxIdle   = 10;
    private Integer maxTotal  = 8;
    private Integer maxActive = 10;
    private Integer timeout   = 300;
    private Long    maxWait   = 30000L;
    private Long    exptime   = 600L;
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Integer getPort() {
        return port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
    }
    
    public Integer getDatabase() {
        return database;
    }
    
    public void setDatabase(Integer database) {
        this.database = database;
    }
    
    public Integer getMinIdle() {
        return minIdle;
    }
    
    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
    
    public Integer getMaxIdle() {
        return maxIdle;
    }
    
    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }
    
    public Integer getMaxTotal() {
        return maxTotal;
    }
    
    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public Integer getMaxActive() {
        return maxActive;
    }
    
    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }
    
    public Integer getTimeout() {
        return timeout;
    }
    
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    
    public Long getMaxWait() {
        return maxWait;
    }
    
    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }
    
    public Long getExptime() {
        return exptime;
    }
    
    public void setExptime(Long exptime) {
        this.exptime = exptime;
    }
}
