package xyz.launcel.service;

import xyz.launcel.dao.IdEntity;
import xyz.launcel.dao.Page;

/**
 * @author launcel
 */
public interface BaseService
{

    <T extends IdEntity> int add(T o);

    <T extends IdEntity> int update(T o);

    <T extends IdEntity> T get(Integer id);

    int delete(Integer id);

    <T extends IdEntity> T query(Object o);

    <T extends IdEntity> Integer count(Object o);

    <T extends IdEntity> Page<T> queryPage(Object o, Page<T> page);

}
