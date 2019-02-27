package xyz.launcel.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "job.scheduler")
public class SchedulerPoolProperties
{
    private Boolean enabled  = false;
    private Integer poolSize = 10;
}
