package xyz.launcel.dao.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.lang.NonNull;
import xyz.launcel.bean.SpringBeanUtil;
import xyz.launcel.dao.BaseDao;
import xyz.launcel.enumerate.BatchType;
import xyz.launcel.utils.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class BatchUtils
{

    private static SqlSessionFactory defaultSqlSessionFactory = SpringBeanUtil.getBean("sqlSessionFactory");

    private static ThreadLocal<SqlSessionFactory> localSqlSessionFactory = null;

    public static void setSqlSessionFactory(@NonNull SqlSessionFactory sqlSessionFactory)
    {
        localSqlSessionFactory = new ThreadLocal<>();
        localSqlSessionFactory.set(sqlSessionFactory);
    }

    public static <T> int batchAdd(List<T> list, Class<? extends BaseDao> mapper)
    {
        return execute(list, mapper, BatchType.INSERT);
    }

    public static <T> int batchUpdate(List<T> list, Class<? extends BaseDao> mapper)
    {
        return execute(list, mapper, BatchType.UPDATE);
    }

    public static int batchDel(List<Integer> ids, Class<? extends BaseDao> mapper)
    {
        return execute(ids, mapper, BatchType.DELETE);
    }

    private static <T> int execute(List<T> list, Class<? extends BaseDao> mapper, BatchType type)
    {
        if (CollectionUtils.isEmpty(list))
        {
            return 0;
        }
        var size       = list.size();
        var result     = 0;
        var tmpFactory = Objects.isNull(localSqlSessionFactory) ? defaultSqlSessionFactory : localSqlSessionFactory.get();
        var session    = tmpFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            var repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                var l = list.get(i);
                switch (type)
                {
                    case INSERT:
                        result += repository.add(l);
                        break;
                    case UPDATE:
                        result += repository.update(l);
                        break;
                    case DELETE:
                        result += repository.delete((Integer) l);
                        break;
                    default:
                        break;
                }
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
            if (Objects.nonNull(localSqlSessionFactory))
            {
                localSqlSessionFactory.remove();
            }
        }
        return result;
    }
}
