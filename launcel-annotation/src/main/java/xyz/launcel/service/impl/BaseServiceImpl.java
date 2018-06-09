package xyz.launcel.service.impl;

import xyz.launcel.dao.BaseDAO;
import xyz.launcel.dao.Page;
import xyz.launcel.service.BaseService;

import java.util.List;

/**
 * @author launcel
 */
public abstract class BaseServiceImpl implements BaseService
{

    protected abstract <T> BaseDAO getDAO();

    @Override
    public <T> int add(T t)
    {
        return getDAO().add(t);
    }

    @Override
    public <T> int update(T t)
    {
        return getDAO().update(t);
    }

    @Override
    public int delete(Integer id)
    {
        return getDAO().delete(id);
    }

    @Override
    public <T> T query(T t)
    {
        return getDAO().query(t);
    }

    @Override
    public <T> T get(Integer id)
    {
        return getDAO().get(id);
    }

    @Override
    public <T> Integer count(T t)
    {
        return getDAO().count(t);
    }

    @Override
    public <T> Page<T> queryPage(T t, Page<T> page)
    {
        Integer total = getDAO().count(t);
        if (total != null && total > 0)
        {
            List<T> list = getDAO().queryPage(t, page);
            page.setTotal(total);
            page.setList(list);
        }
        return page;
    }

}
