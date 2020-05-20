package xyz.launcel.redis.support.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RedisLockLocal
{
    private boolean hasLock = false;
    private String  key;
    private long    time;
    private long    startTime;
}
