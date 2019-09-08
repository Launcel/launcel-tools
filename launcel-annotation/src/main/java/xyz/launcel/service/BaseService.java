package xyz.launcel.service;

import xyz.launcel.bo.PageQuery;
import xyz.launcel.dao.IdEntity;
import xyz.launcel.response.Page;

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

    Long count(Object o);

    <T> Page<T> queryPage(Object o, PageQuery page);
}
