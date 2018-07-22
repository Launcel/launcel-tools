package xyz.launcel.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author launcel
 */
public interface BaseRepository
{

    <T extends IdEntity> int add(T o);

    <T extends IdEntity> int update(T o);

    <T extends IdEntity> T get(Integer id);

    int delete(Integer id);

    <T extends IdEntity> T query(@Param("param") Object o);

    Integer count(@Param("param") Object o);

    <T extends IdEntity> List<T> queryPage(@Param("param") Object o, @Param("page") Page<T> page);

}