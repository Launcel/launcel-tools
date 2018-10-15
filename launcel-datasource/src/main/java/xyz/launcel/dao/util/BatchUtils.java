package xyz.launcel.dao.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import xyz.launcel.bean.context.SpringBeanUtil;
import xyz.launcel.dao.BaseRepository;
import xyz.launcel.enumerate.BatchType;
import xyz.launcel.lang.CollectionUtils;

import java.util.List;

public class BatchUtils
{
    private static SqlSessionFactory sqlSessionFactory = SpringBeanUtil.getBean("sqlSessionFactory");

    static <T> int batchAdd(List<T> list, Class<? extends BaseRepository> mapper)
    {
        return execute(list, mapper, BatchType.INSERT);
    }

    static <T> int batchUpdate(List<T> list, Class<? extends BaseRepository> mapper)
    {
        return execute(list, mapper, BatchType.UPDATE);
    }

    static int batchDel(List<Integer> ids, Class<? extends BaseRepository> mapper)
    {
        return execute(ids, mapper, BatchType.DELETE);
    }

    private static <T> int execute(List<T> list, Class<? extends BaseRepository> mapper, BatchType type)
    {
        if (CollectionUtils.isEmpty(list))
            return 0;
        var size    = list.size();
        var result  = 0;
        var session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
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
        }
        return result;
    }

}
