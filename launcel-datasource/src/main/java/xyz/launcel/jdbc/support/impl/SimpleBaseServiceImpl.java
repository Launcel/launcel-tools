package xyz.launcel.jdbc.support.impl;

import xyz.launcel.bo.PageQuery;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.jdbc.interceptor.PageHelper;
import xyz.launcel.jdbc.support.SimpleBaseService;
import xyz.launcel.response.Page;
import xyz.launcel.service.impl.BaseServiceImpl;

import java.util.List;

public abstract class SimpleBaseServiceImpl extends BaseServiceImpl implements SimpleBaseService
{
    @Override
    public <T> Page<T> queryPage(Object o, PageQuery pageQuery)
    {
        com.github.pagehelper.Page<?> page = PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());

        List<T> list = getMapper().queryPage(o, pageQuery);
        if (page.getTotal() == 0 || CollectionUtils.isEmpty(list))
        {
            return new Page<>();
        }
        return new Page<>(page.getTotal(), list, pageQuery.getPageNum(), page.getPageSize());
    }
}
