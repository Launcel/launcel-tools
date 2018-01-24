package xyz.launcel.service.impl;

import xyz.launcel.dao.BaseDAO;
import xyz.launcel.dao.Paging;
import xyz.launcel.log.BaseLogger;
import xyz.launcel.service.IBaseService;

import java.util.List;

public abstract class BaseService<T> extends BaseLogger implements IBaseService<T> {

    protected abstract BaseDAO<T> getDAO();

    @Override
    public int insert(T t) {
        return getDAO().insert(t);
    }

    @Override
    public int update(T t) {
        return getDAO().update(t);
    }

    @Override
    public int delete(Integer t) {
        return getDAO().delete(t);
    }

    @Override
    public T select(T t) {
        return getDAO().select(t);
    }

    @Override
    public T get(Integer id) {
        return getDAO().get(id);
    }

    @Override
    public Integer count(T t) {
        return getDAO().count(t);
    }

    @Override
    public void getPaging(T t, Paging<T> paging) {
        Integer total = getDAO().count(t);
        if (total == null || total == 0)
            return;
        paging.setTotal(total);
        List<T> list = getDAO().queryPaging(t, paging);
        paging.setData(list);
    }

}
