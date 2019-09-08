package xyz.launcel.response;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author launcel
 */
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class Page<T> implements Serializable
{
    private static final long serialVersionUID = -8522433864030332281L;

    private           List<T> list;
    private           Integer pageNo    = 1;
    private transient Integer row       = 15;
    private           Integer totalPage = 0;
    private           Long    total     = 0L;


    public Page(Long total, List<T> list, Integer row)
    {
        this.list = list;
        this.total = total;
        this.row = row;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    public void setRow(Integer row)
    {
        this.row = row;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }

    public void setPageNo(Integer pageNo)
    {
        this.pageNo = pageNo;
    }

    public void setTotalPage(Integer totalPage)
    {
        this.totalPage = totalPage;
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

    public Integer getPageNo()
    {
        return (null == pageNo || pageNo < 1) ? 1 : pageNo;
    }

    public Integer getRow()
    {
        return Objects.isNull(row) || row < 0 ? 15 : row;
    }

    public Integer getTotalPage()
    {
        return (int) Math.ceil(getTotal() * 1.0D / this.getRow());
    }

    public Long getTotal()
    {
        return Objects.isNull(total) ? 0 : total;
    }
}