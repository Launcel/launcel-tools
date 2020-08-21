package xyz.x.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "job.scheduler")
public class SchedulerPoolProperties
{
    private Integer poolSize = 10;
}
