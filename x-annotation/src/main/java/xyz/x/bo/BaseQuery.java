package xyz.x.bo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import xyz.x.annotation.StatusType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class BaseQuery extends PageQuery
{
    private Integer       id;
    private List<Integer> ids;
    private StatusType    status = StatusType.ENABLED;

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

    public int getStatus()
    {
        return status.getStstus();
    }

    public void setStatus(StatusType status)
    {
        this.status = status;
    }
}
