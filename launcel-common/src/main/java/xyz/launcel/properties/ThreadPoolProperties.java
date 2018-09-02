package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by launcel on 2018/8/6.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "task.executor")
public class ThreadPoolProperties
{
    private Integer corePoolSize  = 1;
    private Integer maxPoolSize   = 8;
    private Integer queueCapacity = 10;
}
