package xyz.launcel.lang;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 64位生成规则【首位是符号位，代表正副，所以实际有效是63位】：id长度为18位
 * <p>
 * 41位时间戳 |10位进程号 |12位计数器
 * <p>
 * 41位时间戳：
 * 10位机器：最多1023台机器
 * 12位计数器：每毫秒最多生成4095条记录
 * <p>
 * 这里可以根据项目中实际情况，调整每个位置的长度，比如分布式集群机器数量比较少，可以缩减机器的位数，增加计数器位数
 * <p>
 * <p>
 * 使用注意事项：1、必须关闭时钟同步；2、currentTimeMillis一经定义，不可修改；3、并发量太高的时候，超过了4095，未做处理；4、最大不超过64位
 * <p>
 * 自测性能：一秒能有三四十万的数据产生。
 */
public final class IDGeneratorUtil
{
    private IDGeneratorUtil() {}

    /**
     * 2017-12-01 0:00:00
     */
    private static final long initTimeMillis = 1512057600000L;

    /**
     * 机器编号
     */
    private static final int pid = 3;

    /**
     * 计数器
     * 需要保证线程安全
     */
    private static AtomicLong counter = new AtomicLong(0);

    private static volatile long currentTimeMillis = System.currentTimeMillis() - initTimeMillis;
    private static volatile long lastTimeMillis    = currentTimeMillis;

    public static Long nextId()
    {
        long series = counter.getAndIncrement();

        if (series >= (1 << 12) - 1)
        {
            while (lastTimeMillis == currentTimeMillis)
            {//等待到下一秒
                currentTimeMillis = System.currentTimeMillis() - initTimeMillis;
            }
            lastTimeMillis = currentTimeMillis;
            counter.set(0);
            series = counter.getAndIncrement();
        }
        return (currentTimeMillis << 22) | (pid << 12) | series;
    }

}
