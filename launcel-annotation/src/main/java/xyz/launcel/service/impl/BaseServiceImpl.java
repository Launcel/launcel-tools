package xyz.launcel.service.impl;

import xyz.launcel.dao.BaseRepository;
import xyz.launcel.dao.IdEntity;
import xyz.launcel.dao.Page;
import xyz.launcel.service.BaseService;

import java.util.List;

/**
 * @author launcel
 */
public abstract class BaseServiceImpl implements BaseService
{

    protected abstract BaseRepository getMapper();

    @Override
    public <T extends IdEntity> int add(T p)
    {
        getMapper().add(p);
        return p.getId();
    }

    @Override
    public <T> int update(T p)
    {
        return getMapper().update(p);
    }

    @Override
    public int delete(Integer id)
    {
        return getMapper().delete(id);
    }

    @Override
    public Integer count(Object o)
    {
        return getMapper().count(o);
    }

    @Override
    public <T> T get(Integer id)
    {
        return getMapper().get(id);
    }

    @Override
    public <T> T query(Object o)
    {
        return getMapper().query(o);
    }

    @Override
    public <T> Page<T> queryPage(Object o, Page<T> page)
    {
        Integer total = getMapper().count(o);
        if (total != null && total > 0)
        {
            List<T> list = getMapper().queryPage(o, page);
            page.setTotal(total);
            page.setList(list);
        }
        return page;
    }
}
