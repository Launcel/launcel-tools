package xyz.launcel.service.impl;

import lombok.var;
import xyz.launcel.bo.PageQuery;
import xyz.launcel.dao.BaseDao;
import xyz.launcel.dao.IdEntity;
import xyz.launcel.response.Page;
import xyz.launcel.service.BaseService;

import java.util.List;

/**
 * @author launcel
 */
public abstract class BaseServiceImpl implements BaseService
{

    protected abstract BaseDao getMapper();

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
    public Long count(Object o)
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
    public <T> Page<T> queryPage(Object o, PageQuery pageQuery)
    {
        Page<T> page  = new Page<>();
        var     total = getMapper().count(o);
        if (total != null && total > 0)
        {
            List<T> list = getMapper().queryPage(o, pageQuery);
            page.setTotal(total);
            page.setList(list);
        }
        return page;
    }
}
