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

    static <T> void batchAdd(List<T> list, Class<? extends BaseRepository> mapper)
    {
        if (CollectionUtils.isEmpty(list))
            return;
        int        size    = list.size();
        int        step    = 0;
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            BaseRepository repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                T t = list.get(i);
                if (step < 99 || i >= size - 1)
                {
                    repository.add(t);
                    session.commit();
                    session.clearCache();
                    step = 0;
                }
                step++;
            }
        }
        catch (Exception x)
        {
            session.rollback();
        }
        finally
        {
            session.close();
        }
    }

    static <T> void batchUpdate(List<T> list, Class<? extends BaseRepository> mapper)
    {
        if (CollectionUtils.isEmpty(list))
            return;
        int        size    = list.size();
        int        step    = 0;
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            BaseRepository repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                T t = list.get(i);
                if (step < 99 || i >= size - 1)
                {
                    repository.update(t);
                    session.commit();
                    session.clearCache();
                    step = 0;
                }
                step++;
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
    }

    static void batchDel(List<Integer> ids, Class<? extends BaseRepository> mapper)
    {
        if (CollectionUtils.isEmpty(ids))
            return;
        int        size    = ids.size();
        int        step    = 0;
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try
        {
            BaseRepository repository = session.getMapper(mapper);
            for (int i = 0; i < size; i++)
            {
                Integer id = ids.get(i);
                if (step < 99 || i >= size - 1)
                {
                    repository.delete(id);
                    session.commit();
                    session.clearCache();
                    step = 0;
                }
                step++;
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
    }

}
