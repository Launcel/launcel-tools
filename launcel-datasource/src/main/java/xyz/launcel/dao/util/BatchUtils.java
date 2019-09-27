package xyz.launcel.dao.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.lang.NonNull;
import xyz.launcel.dao.BaseDao;
import xyz.launcel.enumerate.BatchType;
import xyz.launcel.utils.CollectionUtils;

import java.util.List;

public class BatchUtils
{

    private SqlSessionFactory sqlSessionFactory;

    public static BatchUtils newBatch(@NonNull SqlSessionFactory sqlSessionFactory)
    {
        return new BatchUtils(sqlSessionFactory);
    }

    private BatchUtils(SqlSessionFactory defaultSqlSessionFactory)
    {
        this.sqlSessionFactory = defaultSqlSessionFactory;
    }

    public <T> int batchAdd(List<T> list, Class<? extends BaseDao> mapper)
    {
        return execute(list, mapper, BatchType.INSERT);
    }

    public <T> int batchUpdate(List<T> list, Class<? extends BaseDao> mapper)
    {
        return execute(list, mapper, BatchType.UPDATE);
    }

    public int batchDel(List<Integer> ids, Class<? extends BaseDao> mapper)
    {
        return execute(ids, mapper, BatchType.DELETE);
    }

    private <T> int execute(List<T> list, Class<? extends BaseDao> mapper, BatchType type)
    {
        if (CollectionUtils.isEmpty(list))
        {
            return 0;
        }
        var size    = list.size();
        var result  = 0;
        var session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            var repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                var l = list.get(i);
                result += type.execute(l, repository);
                /**
                 * 每100条数据做一次提交
                 */
                if (i % 100 == 0 || i == size - 1)
                {
                    session.clearCache();
                    session.commit();
                }
            }
        }
        catch (Exception x)
        {
            session.rollback();
        }
        finally
        {
            session.clearCache();
            session.close();
            this.sqlSessionFactory = null;
        }
        return result;
    }
}
