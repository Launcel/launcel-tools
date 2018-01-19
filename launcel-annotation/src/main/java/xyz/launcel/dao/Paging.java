package xyz.launcel.dao;

import java.util.List;

public class Paging<T> {

    private int _DEFAULT_ROW = 15;

    private boolean count = true;

    private List<T> data;

    private int maxRow = _DEFAULT_ROW;

    private Integer lowerId;

    private Integer largeId;

    private Integer pageNo = 1;

    /**
     * 总页数（不是数据总数）
     */
    private Integer total;

    public Paging(int maxRow) {
        this.pageNo = 1;
        setMaxRow(maxRow);
    }

    public Paging(int pageNo, int maxRow) {
        setPageNo(pageNo);
        setMaxRow(maxRow);
    }

    public Paging(Integer pageNo, Integer maxRow, Integer lowerId, Integer largeId) {
        setPageNo(pageNo);
        setMaxRow(maxRow);
        setLowerId(lowerId);
        setLargeId(largeId);
    }

    /**
     * 查上一页
     * 使用 ID 做为分页的偏移量
     *
     * @param lowerId [description]
     * @param maxRow  [description]
     */
    public void prePaging(Integer lowerId, Integer maxRow) {
        setLowerId(lowerId);
        setMaxRow(maxRow);
    }

    /**
     * 查下一页
     * 使用 ID 做为分页的偏移量
     *
     * @param largeId
     * @param maxRow
     */
    public void nextPaging(Integer largeId, Integer maxRow) {
        setLargeId(largeId);
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

    public int getNext() {
        return (this.pageNo < getTotal()) ? this.pageNo + 1 : getTotal();
    }

    public int getPrev() {
        return (this.pageNo > 1) ? this.pageNo - 1 : 1;
    }

    /*-------------------getter-----------------------*/

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getMaxRow() {
        return this.maxRow;
    }

    public void setMaxRow(Integer maxRow) {
        this.maxRow = (maxRow == null || maxRow < 5 || maxRow > 20) ? _DEFAULT_ROW : maxRow;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = (null == pageNo || pageNo < 1) ? _DEFAULT_ROW : pageNo;
    }

    public int getSize() {
        return this.data.size();
    }

    public int getTotal() {
        return this.total;
    }

   /*-------------------setter-----------------------*/

    public void setTotal(Integer total) {
        this.total = (getMaxRow() == 0) ? 1 : (int) Math.ceil(total * 1.0D / this.maxRow);
    }

    public Integer getLargeId() {
        return largeId;
    }

    public void setLargeId(Integer largeId) {
        this.largeId = (null == largeId || largeId < 0) ? 1 : largeId;
    }

    public Integer getLowerId() {
        return lowerId;
    }

    public void setLowerId(Integer lowerId) {
        this.lowerId = (null == lowerId || lowerId < 0) ? 1 : lowerId;
    }

    public boolean isCount() {
        return this.count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }
}