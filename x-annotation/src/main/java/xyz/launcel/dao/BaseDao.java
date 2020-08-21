package xyz.launcel.dao;

import org.apache.ibatis.annotations.Param;
import xyz.launcel.bo.PageQuery;

import java.util.List;

/**
 * @author launcel
 */
public interface BaseDao
{

    <T> int add(T o);

    <T> int update(T o);

    <T> T get(Integer id);

    int delete(Integer id);

    <T> T query(@Param("param") Object o);

    Long count(@Param("param") Object o);

    <T> List<T> queryPage(@Param("param") Object o, @Param("page") PageQuery page);
}