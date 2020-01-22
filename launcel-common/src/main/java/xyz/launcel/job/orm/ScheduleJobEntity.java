package xyz.launcel.job.orm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.launcel.dao.IdEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleJobEntity extends IdEntity
{
    private static final long serialVersionUID = 4138548560820488130L;

    private String jobName;
    private String cron;
    private Short  status;
}
