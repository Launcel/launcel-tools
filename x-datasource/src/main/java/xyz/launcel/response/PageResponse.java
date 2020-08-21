package xyz.launcel.response;

import java.util.List;

public class PageResponse<T> extends Page<T>
{
    private static final long serialVersionUID = 2236191649037672435L;

    public PageResponse() { }

    public PageResponse(com.github.pagehelper.Page<?> page)
    {
        super.setPageNum(page.getPageNum());
        super.setPageSize(page.getPageSize());
        super.setTotal(page.getTotal());
    }

    public PageResponse(Page<T> page)
    {
        super.setPageNum(page.getPageNum());
        super.setPageSize(page.getPageSize());
        super.setList(page.getList());
        super.setTotal(page.getTotal());
    }

    @Override
    public List<T> getList()
    {
        return super.getList();
    }

    @Override
    public Integer getPageNum()
    {
        return super.getPageNum();
    }

    @Override
    public Integer getPageSize()
    {
        return super.getPageSize();
    }

    @Override
    public Integer getPageTotal()
    {
        return super.getPageTotal();
    }

    @Override
    public Long getTotal()
    {
        return super.getTotal();
    }
}
