package xyz.launcel.dao.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import xyz.launcel.bean.context.SpringBeanUtil;
import xyz.launcel.dao.BaseRepository;
import xyz.launcel.lang.CollectionUtils;

import java.util.List;

public class BatchUtils
{
    private static SqlSessionFactory sqlSessionFactory = SpringBeanUtil.getBean("sqlSessionFactory");

    static <T> int batchAdd(List<T> list, Class<? extends BaseRepository> mapper)
    {
        return batch(list, mapper, 1);
    }

    static <T> int batchUpdate(List<T> list, Class<? extends BaseRepository> mapper)
    {
        return batch(list, mapper, 2);
    }

    static int batchDel(List<Integer> ids, Class<? extends BaseRepository> mapper)
    {
        return batch(ids, mapper, 3);
    }

    private static <T> int batch(List<T> list, Class<? extends BaseRepository> mapper, int type)
    {
        if (CollectionUtils.isEmpty(list))
            return 0;
        int        size    = list.size();
        int        step    = 0;
        int        result  = 0;
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            BaseRepository repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                T l = list.get(i);
                if (step < 99)
                {
                    switch (type)
                    {
                        case 1:
                            result += repository.add(l);
                            break;
                        case 2:
                            result += repository.update(l);
                            break;
                        case 3:
                            result += repository.delete((Integer) l);
                            break;
                        default:
                            break;
                    }
                    step++;
                }
                if (step >= 100 || i == size - 1)
                {
                    session.commit();
                    session.clearCache();
                    step = 0;
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
