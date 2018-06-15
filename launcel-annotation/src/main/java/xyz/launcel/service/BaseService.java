package xyz.launcel.service;

import xyz.launcel.dao.Paging;

public interface BaseService<T> {

    int add(T t);

    int update(T t);

    int delete(Integer id);

    T query(T t);

    T get(Integer id);

    Integer count(T t);

    void queryPage(T t, Paging<T> paging);

}
