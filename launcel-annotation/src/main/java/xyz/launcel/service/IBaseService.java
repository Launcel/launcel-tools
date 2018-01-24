package xyz.launcel.service;

import xyz.launcel.dao.Paging;

public interface IBaseService<T> {

    int insert(T t);

    int update(T t);

    int delete(Integer id);

    T select(T t);

    T get(Integer id);

    Integer count(T t);

    void getPaging(T t, Paging<T> paging);

}
