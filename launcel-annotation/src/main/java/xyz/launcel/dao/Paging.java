package xyz.launcel.dao;

import java.io.Serializable;
import java.util.List;

public class Paging<T> implements Serializable {
    private static final long serialVersionUID = -8522433864030332281L;

    private List<T> list;

    private int maxRow = 15;

    private Integer pageNo = 1;

    /**
     * 总页数（不是数据总数）
     */
    private Integer total = 0;

    public Paging() {
    }

    public Paging(int maxRow) {
        this.pageNo = 1;
        setMaxRow(maxRow);
    }

    public Paging(int pageNo, int maxRow) {
        setPageNo(pageNo);
        setMaxRow(maxRow);
    }


    /**
     * 普通分页查询，获得偏移量
     *
     * @return [description]
     */

    public int getOffset() {
        return (this.pageNo - 1) * this.maxRow;
    }


    /*-------------------getter-----------------------*/

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getMaxRow() {
        return this.maxRow;
    }

    public void setMaxRow(Integer maxRow) {
        this.maxRow = (maxRow == null || maxRow < 1) ? 15 : maxRow;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = (null == pageNo || pageNo < 1) ? 1 : pageNo;
    }

    public int getTotal() {
        return this.total;
    }

    /*-------------------setter-----------------------*/

    public void setTotal(Integer total) {
        this.total = (getMaxRow() == 0) ? 1 : (int) Math.ceil(total * 1.0D / this.maxRow);
    }

}