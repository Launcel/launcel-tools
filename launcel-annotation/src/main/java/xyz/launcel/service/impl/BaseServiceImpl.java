package xyz.launcel.service.impl;

import xyz.launcel.dao.BaseRepository;
import xyz.launcel.dao.Page;
import xyz.launcel.service.BaseService;

import java.util.List;

/**
 * @author launcel
 */
public abstract class BaseServiceImpl implements BaseService
{

    protected abstract BaseRepository getRepository();

    @Override
    public <T, P> int add(P p)
    {
        return getRepository().add(p);
    }

    @Override
    public <T, P> int update(P p)
    {
        return getRepository().update(p);
    }

    @Override
    public int delete(Integer id)
    {
        return getRepository().delete(id);
    }

    @Override
    public <T, P> T query(P p)
    {
        return getRepository().query(p);
    }

    @Override
    public <T> T get(Integer id)
    {
        return getRepository().get(id);
    }

    @Override
    public <T, P> Integer count(P p)
    {
        return getRepository().count(p);
    }

    @Override
    public <T, P> Page<T> queryPage(P p, Page<T> page)
    {
        Integer total = getRepository().count(p);
        if (total != null && total > 0)
        {
            List<T> list = getRepository().queryPage(p, page);
            page.setTotal(total);
            page.setList(list);
        }
        return page;
    }

}
