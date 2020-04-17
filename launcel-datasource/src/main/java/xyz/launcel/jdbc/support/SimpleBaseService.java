package xyz.launcel.jdbc.support;

import xyz.launcel.bo.PageQuery;
import xyz.launcel.response.Page;
import xyz.launcel.service.BaseService;

public interface SimpleBaseService extends BaseService
{
    <T> Page<T> queryPage(Object o, PageQuery page);
}
