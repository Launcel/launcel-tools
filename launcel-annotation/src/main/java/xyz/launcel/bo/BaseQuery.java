package xyz.launcel.bo;

import lombok.NoArgsConstructor;
import xyz.launcel.annotation.StatusType;

import java.util.List;

/**
 * Created by launcel on 2018/9/23.
 */
@NoArgsConstructor
public class BaseQuery
{
    private Integer       id;
    private List<Integer> ids;
    private int           status = StatusType.ENABLED.getStstus();

    public BaseQuery(Integer id, List<Integer> ids, StatusType status)
    {
        this.id = id;
        this.ids = ids;
        this.status = status.getStstus();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<Integer> getIds()
    {
        return ids;
    }

    public void setIds(List<Integer> ids)
    {
        this.ids = ids;
    }

    public StatusType getStatus()
    {
        return StatusType.valueOf(status);
    }

    public void setStatus(StatusType status)
    {
        this.status = status.getStstus();
    }
}
