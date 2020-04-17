package xyz.launcel.bo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import xyz.launcel.annotation.OrderSqlEnum;
import xyz.launcel.request.PageRequest;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class PageQuery
{
    private Integer                             pageNum  = 1;
    private Integer                             pageSize = 15;
    private Date                                startTime;
    private Date                                endTime;
    private LinkedHashMap<String, OrderSqlEnum> orderBy;
    private Set<String>                         groupBy;

    /**
     * 普通分页查询，获得偏移量
     */
    public int getOffset()
    {
        return (this.pageNum - 1) * this.pageSize;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNo)
    {
        this.pageNum = pageNo;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer row)
    {
        this.pageSize = row;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public LinkedHashMap<String, OrderSqlEnum> getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(LinkedHashMap<String, OrderSqlEnum> orderBy)
    {
        this.orderBy = orderBy;
    }

    public Set<String> getGroupBy()
    {
        return groupBy;
    }

    public void setGroupBy(Set<String> groupBy)
    {
        this.groupBy = groupBy;
    }

    public PageQuery(PageRequest request)
    {
        this.pageNum = request.getPageNo();
        this.pageSize = request.getRow();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
    }

    public PageQuery addOrderBy(String key, OrderSqlEnum order)
    {
        if (Objects.isNull(orderBy))
        {
            orderBy = new LinkedHashMap<>();
        }
        orderBy.put(key, order);
        return this;
    }

    public PageQuery addGroupBy(String group)
    {
        if (Objects.isNull(groupBy))
        {
            groupBy = new HashSet<>();
        }
        groupBy.add(group);
        return this;
    }
}
