package xyz.x.redis.support.lock;

import xyz.x.common.ensure.Predict;
import xyz.x.redis.support.RedisUtils;

import java.lang.reflect.Method;

public abstract class RedisSync<T>
{
    private String key;
    private long   time;
    private long   START_TIME;
    private T      result;

    public T get()
    {
        return result;
    }

    public abstract String key();

    protected abstract T apply();

    public RedisSync()
    {
        init();
        work();
    }

    private void init()
    {
        try
        {
            Method keyMethod = getClass().getMethod("key");
            Predict.builder(keyMethod).isNull("找不到 key 方法");

            Lock lock = keyMethod.getAnnotation(Lock.class);
            Predict.builder(lock).isNull("Lock 加锁注解不存在");
            Predict.builder(lock.body()).isBlank("Lock 加锁的 body 不存在");
            Predict.builder(lock.time()).ltOrEq(0L).isTrue("Lock 加锁的失效时间不能设置为永久或者不能为0");
            Predict.builder(key()).isBlank("加锁的 key 不存在");

            key = lock.body().concat(key());
            time = lock.time() * 1000000;
            START_TIME = System.nanoTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    private void work()
    {
        long now = System.nanoTime();

        while (time > now - START_TIME)
        {
            if (lock())
            {
                try
                {
                    result = apply();
                    break;
                }
                finally
                {
                    unlock();
                }
            }
            now = System.nanoTime();
        }
    }

    private boolean lock()
    {
        return RedisUtils.setNxNANO(key, "1", time);
    }

    private void unlock()
    {
        //        if (TransactionSynchronizationManager.isActualTransactionActive())
        //        {
        //            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter()
        //            {
        //                @Override
        //                public void afterCompletion(int status)
        //                {
        //                    super.afterCompletion(status);
        //                }
        //            });
        //        }
        RedisUtils.unclock(key);
    }
}
