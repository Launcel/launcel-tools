package xyz.launcel.response;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author launcel
 */
@Setter
@NoArgsConstructor
@ToString
class Page<T> implements Serializable
{
    private static final long serialVersionUID = -8522433864030332281L;

    private           List<T> list;
    private           Integer pageNum   = 1;
    private transient Integer pageSize  = 15;
    private           Integer pageTotal = 0;
    private           Long    total     = 0L;


    public Page(Long total, List<T> list, Integer row)
    {
        this.list = list;
        this.total = total;
        this.pageSize = row;
    }

    /*************************  getter  ***************************/

    public List<T> getList()
    {
        if (Objects.isNull(list))
        {
            return new ArrayList<>();
        }
        return list;
    }

    public Integer getPageNum()
    {
        return Objects.isNull(pageNum) ? 1 : pageNum;
    }

    public Integer getPageSize()
    {
        return Objects.isNull(pageSize) ? 15 : pageSize;
    }

    public Integer getPageTotal()
    {
        return (int) Math.ceil(getTotal() * 1.0D / this.getPageSize());
    }

    public Long getTotal()
    {
        return Objects.isNull(total) ? 0 : total;
    }
}