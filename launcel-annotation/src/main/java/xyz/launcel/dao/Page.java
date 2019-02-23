package xyz.launcel.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import xyz.launcel.annotation.OrderSqlEnum;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @param <T>
 *
 * @author launcel
 */
@Accessors(chain = true)
@NoArgsConstructor
@ToString(exclude = {"row", "minId", "maxId", "offset", "orderBy", "groupBy"})
public class Page<T> implements Serializable
{
    private static final long serialVersionUID = -8522433864030332281L;

    @Getter
    @Setter
    private           List<T> list;
    @Getter
    private transient Integer row = Integer.MAX_VALUE;

    @Getter
    private transient Integer minId;
    @Getter
    private transient Integer maxId;
    @Getter
    private           Integer pageNo = 1;

    /**
     * 总页数（不是数据总数）
     */
    private Integer total = 0;

    private transient int offset;

    @Setter
    @Getter
    private transient LinkedHashMap<String, OrderSqlEnum> orderBy;

    @Getter
    private transient Set<String> groupBy;

    public Page(int row)
    {
        this.row = row;
    }

    public Page(Integer pageNo, Integer row)
    {
        this.pageNo = pageNo;
        this.row = row;
    }

    //    public Page(int row, int lowerId)
    //    {
    //        this.row = row;
    //        this.lowerId = lowerId;
    //    }
    //
    //    public Page(int row, Integer largeId)
    //    {
    //        this.row = row;
    //        this.largeId = largeId;
    //    }

    public Page(Integer total, List<T> list)
    {
        this.list = list;
        this.total = total;
    }

    /*-------------------getter-----------------------*/

    /**
     * 普通分页查询，获得偏移量
     *
     * @return [description]
     */

    public int getOffset()
    {
        return (this.pageNo - 1) * this.row;
    }

    /*-------------------setter-----------------------*/

    public Page<T> setRow(Integer row)
    {
        this.row = (row == null || row < 5 || row > 25) ? 15 : row;
        return this;
    }

    public Page<T> setPageNo(Integer pageNo)
    {
        this.pageNo = (null == pageNo || pageNo < 1) ? 1 : pageNo;
        return this;
    }

    public Page<T> setTotal(Integer total)
    {
        this.total = (getRow() == 0) ? 1 : (int) Math.ceil(total * 1.0D / this.row);
        return this;
    }

    public void setMinId(Integer minId)
    {
        this.minId = (null == minId || minId < 0) ? 1 : minId;
    }


    public void setMaxId(Integer maxId)
    {
        this.maxId = (null == maxId || maxId < 0) ? 1 : maxId;
    }

    public Page<T> setOrderBy(String orderByCol, OrderSqlEnum orderRule)
    {
        if (Objects.isNull(this.orderBy))
        {
            this.orderBy = new LinkedHashMap<>();
        }
        this.orderBy.put(orderByCol, orderRule);
        return this;
    }

    public Page<T> setGroupBy(String groupBy)
    {
        if (Objects.isNull(this.groupBy))
        {
            this.groupBy = new HashSet<>();
        }
        this.groupBy.add(groupBy);
        return this;
    }
}