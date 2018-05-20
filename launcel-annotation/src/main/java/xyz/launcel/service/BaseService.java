package xyz.launcel.service;

import xyz.launcel.dao.Page;

/**
 * @author launcel
 */
public interface BaseService {
    
    <T> int add(T t);
    
    <T> int update(T t);
    
    int delete(Integer id);
    
    <T> T query(T t);
    
    <T> T get(Integer id);
    
    <T> Integer count(T t);
    
    <T> Page<T> queryPage(T t, Page<T> page);
    
}
