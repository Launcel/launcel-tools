package xyz.x.jdbc.support;

import xyz.launcel.common.exception.BusinessException;
import xyz.launcel.dao.BaseDao;

public enum BatchType implements Batch
{
    INSERT
            {
                @Override
                public <T> int execute(T model, BaseDao mapper)
                {
                    return mapper.add(model);
                }
            },
    UPDATE
            {
                @Override
                public <T> int execute(T model, BaseDao mapper)
                {
                    return mapper.update(model);
                }
            },
    DELETE
            {
                @Override
                public <T> int execute(T model, BaseDao mapper)
                {
                    if (!(model instanceof Integer))
                    { throw new BusinessException("参数类型错误"); }
                    return mapper.delete((Integer) model);
                }
            }
}

interface Batch
{
    <T> int execute(T model, BaseDao mapper);
}
