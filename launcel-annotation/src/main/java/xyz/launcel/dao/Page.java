package xyz.launcel.dao;

import xyz.launcel.annotation.OrderSqlEnum;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author launcel
 * @param <T>
 */
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -8522433864030332281L;
    
    private List<T> list;
    
    private transient Integer maxRow = Integer.MAX_VALUE;
    
    private transient Integer lowerId;
    
    private transient Integer largeId;
    
    private Integer pageNo = 1;
    
    /**
     * 总页数（不是数据总数）
     */
    private Integer total = 0;
    
    private transient int offset;

    private transient LinkedHashMap<String, OrderSqlEnum> orderBy;

    private transient Set<String> groupBy;
    
    public Page() {
    }
    
    public Page(Integer pageNo) {
        this.pageNo = pageNo;
    }
    
    public Page(int maxRow) {
        this.pageNo = 1;
        this.maxRow = maxRow;
    }
    
    public Page(Integer pageNo, Integer maxRow) {
        this.pageNo = pageNo;
        this.maxRow = maxRow;
    }
    
    public Page(int maxRow, int lowerId) {
        this.maxRow = maxRow;
        this.lowerId = lowerId;
    }
    
    public Page(int maxRow, Integer largeId) {
        this.maxRow = maxRow;
        this.largeId = largeId;
    }
    
    public Page(Integer total, List<T> list) {
        this.list = list;
        this.total = total;
    }
    
    /*-------------------getter-----------------------*/
    
    /**
     * 普通分页查询，获得偏移量
     *
     * @return [description]
     */
    
    public int getOffset() {
        return (this.pageNo - 1) * this.maxRow;
    }
    
    public List<T> getList() {
        return list;
    }
    
    
    public int getMaxRow() {
        return this.maxRow;
    }
    
    
    public int getPageNo() {
        return this.pageNo;
    }
    
    
    public int getTotal() {
        return this.total;
    }
    
    public Integer getLargeId() {
        return largeId;
    }
    
    public Integer getLowerId() {
        return lowerId;
    }
    /*-------------------setter-----------------------*/
    
    public void setList(List<T> list) {
        this.list = list;
    }
    
    public void setMaxRow(Integer maxRow) {
        this.maxRow = (maxRow == null || maxRow < 5 || maxRow > 20) ? 15 : maxRow;
    }
    
    public void setPageNo(Integer pageNo) {
        this.pageNo = (null == pageNo || pageNo < 1) ? 15 : pageNo;
    }
    
    public void setTotal(Integer total) {
        this.total = (getMaxRow() == 0) ? 1 : (int) Math.ceil(total * 1.0D / this.maxRow);
    }
    
    public void setLargeId(Integer largeId) {
        this.largeId = (null == largeId || largeId < 0) ? 1 : largeId;
    }
    
    
    public void setLowerId(Integer lowerId) {
        this.lowerId = (null == lowerId || lowerId < 0) ? 1 : lowerId;
    }

    public LinkedHashMap<String, OrderSqlEnum> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderByCol, OrderSqlEnum orderRule) {
        if (Objects.isNull(this.orderBy)) { this.orderBy = new LinkedHashMap<>(); }
        this.orderBy.put(orderByCol, orderRule);
    }

    public void setOrderBy(LinkedHashMap<String, OrderSqlEnum> orderBy) {
        this.orderBy = orderBy;
    }

    public Set<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(Set<String> groupBy) {
        this.groupBy = groupBy;
    }

    public void setGroupBy(String groupBy) {
        if (Objects.isNull(this.groupBy)) { this.groupBy = new HashSet<>(); }
        this.groupBy.add(groupBy);
    }
}