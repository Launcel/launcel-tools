package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author launcel
 */
public interface BaseRepository
{

    <T, P> T query(@Param("param") P p);

    <T, P> int add(P p);

    <T, P> int update(P p);

    int delete(Integer id);

    <T, P> Integer count(@Param("param") P p);

    <T, P> List<T> queryPage(@Param("param") P p, @Param("page") Page<T> page);

    <T> T get(Integer id);

}