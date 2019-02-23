package xyz.launcel.dao.util;

import lombok.var;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import xyz.launcel.bean.context.SpringBeanUtil;
import xyz.launcel.dao.DaoSupport;
import xyz.launcel.enumerate.BatchType;
import xyz.launcel.utils.CollectionUtils;

import java.util.List;

public class BatchUtils
{
    private static SqlSessionFactory sqlSessionFactory = SpringBeanUtil.getBean("sqlSessionFactory");

    static <T> int batchAdd(List<T> list, Class<? extends DaoSupport> mapper)
    {
        return execute(list, mapper, BatchType.INSERT);
    }

    static <T> int batchUpdate(List<T> list, Class<? extends DaoSupport> mapper)
    {
        return execute(list, mapper, BatchType.UPDATE);
    }

    static int batchDel(List<Integer> ids, Class<? extends DaoSupport> mapper)
    {
        return execute(ids, mapper, BatchType.DELETE);
    }

    private static <T> int execute(List<T> list, Class<? extends DaoSupport> mapper, BatchType type)
    {
        if (CollectionUtils.isEmpty(list))
        {
            return 0;
        }
        int size    = list.size();
        int result  = 0;
        var session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            var repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                T l = list.get(i);
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
        }
        return result;
    }

}
