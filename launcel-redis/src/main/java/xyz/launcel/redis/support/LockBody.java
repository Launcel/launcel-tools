package xyz.launcel.redis.support;

public interface LockBody
{
    default String getBody()
    {
        return "";
    }
}
