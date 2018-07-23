package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author launcel
 */
public interface BaseRepository
{

    <T> int add(T o);

    <T> int update(T o);

    <T> T get(Integer id);

    int delete(Integer id);

    <T> T query(@Param("param") Object o);

    Integer count(@Param("param") Object o);

    <T> List<T> queryPage(@Param("param") Object o, @Param("page") Page<T> page);

}