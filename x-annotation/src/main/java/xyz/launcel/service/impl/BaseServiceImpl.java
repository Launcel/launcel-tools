package xyz.launcel.service.impl;

import xyz.launcel.dao.BaseDao;
import xyz.launcel.dao.IdEntity;
import xyz.launcel.service.BaseService;

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

    //    @Override
    //    public <T> Page<T> queryPage(Object o, PageQuery pageQuery)
    //    {
    //        var total = getMapper().count(o);
    //        if (total == null || total == 0)
    //        {
    //            return new Page<>();
    //        }
    //        List<T> list = getMapper().queryPage(o, pageQuery);
    //        return new Page<>(total, list, pageQuery.getRow());
    //    }
}
