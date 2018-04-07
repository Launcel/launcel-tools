package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDAO<T> {

    T select(@Param("param") T t);

    T add(T t);

    int update(T t);

    int delete(Integer t);

    Integer count(@Param("param") T t);

    List<T> queryPaging(@Param("param") T t, @Param("page") Paging<T> page);

    T get(Integer id);

}