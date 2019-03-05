package xyz.launcel.service;

import xyz.launcel.dao.IdEntity;
import xyz.launcel.dao.Page;

/**
 * @author launcel
 */
public interface BaseService
{

    <T extends IdEntity> int add(T o);

    <T> int update(T o);

    <T> T get(Integer id);

    int delete(Integer id);

    <T> T query(Object o);

    <T> Integer count(Object o);

    <T> Page<T> queryPage(Object o, Page<T> page);
}
