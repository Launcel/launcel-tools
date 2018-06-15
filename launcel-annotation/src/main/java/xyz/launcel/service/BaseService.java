package xyz.launcel.service;

import xyz.launcel.dao.Page;

/**
 * @author launcel
 */
public interface BaseService
{

    <T, P> int add(P p);

    <T, P> int update(P p);

    int delete(Integer id);

    <T, P> T query(P p);

    <T> T get(Integer id);

    <T, P> Integer count(P p);

    <T, P> Page<T> queryPage(P p, Page<T> page);

}
