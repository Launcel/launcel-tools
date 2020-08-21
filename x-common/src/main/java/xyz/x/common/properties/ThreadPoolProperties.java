package xyz.x.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * Created by launcel on 2018/8/6.
 */
@Data
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties
{
    private Integer corePoolSize  = 1;
    private Integer maxPoolSize   = 8;
    private Integer queueCapacity = 10;
}
