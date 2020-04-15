package xyz.launcel.redis.support;


import org.springframework.lang.NonNull;
import xyz.launcel.common.ensure.Me;

import java.lang.reflect.Method;

public abstract class RedisLock<T>
{
    private String key;
    private long   time;
    private long   START_TIME;
    private T      result;

    public T get()
    {
        return result;
    }

    @NonNull
    public abstract String key();

    protected abstract T apply();

    public RedisLock()
    {
        init();
        work();
    }

    private void init()
    {
        try
        {
            Method keyMethod = getClass().getMethod("key");
            Me.builder(keyMethod).isNull("找不到 body 方法");
            Lock lock = keyMethod.getAnnotation(Lock.class);
            Me.builder(lock).isNull("Lock 加锁注解不存在");
            Me.builder(lock.body()).isBlank("Lock 加锁的key不存在");
            Me.builder(lock.time() <= 0).isTrue("Lock 加锁的失效时间不能设置为永久");
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
