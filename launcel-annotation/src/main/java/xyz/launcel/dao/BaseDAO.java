package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDAO<T> {

    T select(@Param("param") T t);

    int insert(T t);

    int update(T t);

    Integer count(@Param("param") T t);

    List<T> queryPaging(@Param("param") T t, @Param("page") Paging<T> page);

    int delete(Integer t);

    T get(Integer id);

}