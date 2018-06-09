package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author launcel
 */
public interface BaseDAO
{

    <T> T query(@Param("param") T t);

    <T> int add(T t);

    <T> int update(T t);

    int delete(Integer id);

    <T> Integer count(@Param("param") T t);

    <T> List<T> queryPage(@Param("param") T t, @Param("page") Page<T> page);

    <T> T get(Integer id);

}