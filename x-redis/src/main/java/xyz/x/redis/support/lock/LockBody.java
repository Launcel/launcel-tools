package xyz.x.redis.support.lock;

public interface LockBody
{
    default String getBody()
    {
        return "";
    }
}
