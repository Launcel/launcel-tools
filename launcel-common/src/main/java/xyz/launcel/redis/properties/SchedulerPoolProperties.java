package xyz.launcel.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Data
@ConfigurationProperties(prefix = "job.scheduler")
public class SchedulerPoolProperties
{
    private Boolean enabled  = false;
    private Integer poolSize = 10;

    public void setEnabled(Boolean enabled)
    {
        this.enabled = Objects.nonNull(enabled) ? enabled : false;
    }
}
