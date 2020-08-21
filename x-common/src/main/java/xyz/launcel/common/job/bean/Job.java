package xyz.launcel.common.job.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Job
{
    private Integer id;
    private String  jobName;
    private String  cron;
    private Short   status;

    private transient Runnable work;
}
