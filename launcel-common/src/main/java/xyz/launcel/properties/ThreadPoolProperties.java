package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by launcel on 2018/8/6.
 */
@ConfigurationProperties(prefix = "task.executor")
public class ThreadPoolProperties
{
    private Integer corePoolSize  = 1;
    private Integer maxPoolSize   = 8;
    private Integer queueCapacity = 10;

    public Integer getCorePoolSize()
    {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize)
    {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize()
    {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize)
    {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity()
    {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity)
    {
        this.queueCapacity = queueCapacity;
    }
}
