package xyz.launcel.job.orm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.launcel.dao.IdEntity;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleJobEntity extends IdEntity implements Serializable
{
    private static final long serialVersionUID = 8165621437845112664L;

    private String jobName;
    private String cron;
    private Short  status;
}
