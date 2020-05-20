package xyz.launcel.redis.support.lock;

public interface LockBody
{
    default String getBody()
    {
        return "";
    }
}
