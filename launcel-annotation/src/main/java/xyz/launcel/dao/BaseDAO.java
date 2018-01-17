package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDAO<T> {

    int insert(T t);

    int delete(Integer t);

    T select(@Param("param") T t);

    T selectKey(Integer id);

    int update(T t);

    Integer count(@Param("param") T t);

    List<T> queryPaging(@Param("param") T t, @Param("page") Paging<T> page);

}