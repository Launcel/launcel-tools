package xyz.launcel.service;

import xyz.launcel.dao.Paging;

public interface IBaseService<T> {

    int add(T t);

    int update(T t);

    int delete(Integer id);

    T get(T t);

    T getKey(Integer id);

    Integer count(T t);

    void getPaging(T t, Paging<T> paging);

}
