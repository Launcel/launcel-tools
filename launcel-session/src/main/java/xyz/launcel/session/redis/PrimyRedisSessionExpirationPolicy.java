package xyz.launcel.session.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.ExpiringSession;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PrimyRedisSessionExpirationPolicy {
    private static final Log logger = LogFactory.getLog(PrimyRedisSessionExpirationPolicy.class);

    private final RedisOperations<String, Object> redis;

    private final PrimyRedisOperationsSessionRepository redisSession;

    PrimyRedisSessionExpirationPolicy(RedisOperations<String, Object> sessionRedisOperations, PrimyRedisOperationsSessionRepository redisSession) {
        super();
        this.redis = sessionRedisOperations;
        this.redisSession = redisSession;
    }

    public void onDelete(ExpiringSession session) {
//        long toExpire = roundUpToNextMinute(expiresInMillis(session));
//        String expireKey = getExpirationKey(session.getId(), toExpire);
//        this.redis.boundSetOps(expireKey).remove(session.getId());
    }

    public void onExpirationUpdated(Long originalExpirationTimeInMilli, ExpiringSession session) {
        String keyToExpire = "session:" + session.getId() + ":" + "expires:";
//        long toExpire = roundUpToNextMinute(expiresInMillis(session));

//        if (originalExpirationTimeInMilli != null) {
//            long originalRoundedUp = roundUpToNextMinute(originalExpirationTimeInMilli);
//            if (toExpire != originalRoundedUp) {
//                String expireKey = getExpirationKey(session.getId(), originalRoundedUp);
//                this.redis.boundSetOps(expireKey).remove(keyToExpire);
//            }
//        }

        long sessionExpireInSeconds = session.getMaxInactiveIntervalInSeconds();
        String sessionKey = keyToExpire + session.getId();
        if (sessionExpireInSeconds < 0) {
            this.redis.boundValueOps(sessionKey).append("");
            this.redis.boundValueOps(sessionKey).persist();
            this.redis.boundHashOps(getSessionKey(session.getId())).persist();
            return;
        }

//        String expireKey = getExpirationKey(session.getId(), toExpire);
//        BoundSetOperations<String, Object> expireOperations = this.redis.boundSetOps(expireKey);
//        expireOperations.add(keyToExpire);

        @SuppressWarnings("unused")
        long fiveMinutesAfterExpires = sessionExpireInSeconds + TimeUnit.MINUTES.toSeconds(5);

//        expireOperations.expire(fiveMinutesAfterExpires, TimeUnit.SECONDS);
        if (sessionExpireInSeconds == 0) {
            this.redis.delete(sessionKey);
        } else {
            this.redis.boundValueOps(sessionKey).append("");
            this.redis.boundValueOps(sessionKey).expire(sessionExpireInSeconds, TimeUnit.SECONDS);
        }
        this.redis.boundHashOps(getSessionKey(session.getId())).expire(sessionExpireInSeconds, TimeUnit.SECONDS);
    }

//    private String getExpirationKey(String sessionId, long expires) {
//        return this.redisSession.getExpirationsKey(sessionId, expires);
//    }

    private String getSessionKey(String sessionId) {
        return this.redisSession.getSessionKey(sessionId);
    }

    public void cleanExpiredSessions() {
        long now = System.currentTimeMillis();
        long prevMin = roundDownMinute(now);

        if (logger.isDebugEnabled()) {
            logger.debug("Cleaning up sessions expiring at " + new Date(prevMin));
        }

//        String expirationKey = getExpirationKey(this.privateSessionId, prevMin);
//        Set<Object> sessionsToExpire = this.redis.boundSetOps(expirationKey).members();
//        this.redis.delete(expirationKey);
//        for (Object session : sessionsToExpire) {
//            String sessionKey = getSessionKey((String) session);
//            touch(sessionKey);
//        }
    }

    private void touch(String key) {
        this.redis.hasKey(key);
    }

    private static long expiresInMillis(ExpiringSession session) {
        int maxInactiveInSeconds = session.getMaxInactiveIntervalInSeconds();
        long lastAccessedTimeInMillis = session.getLastAccessedTime();
        return lastAccessedTimeInMillis + TimeUnit.SECONDS.toMillis(maxInactiveInSeconds);
    }

    private static long roundUpToNextMinute(long timeInMs) {

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMs);
        date.add(Calendar.MINUTE, 1);
        date.clear(Calendar.SECOND);
        date.clear(Calendar.MILLISECOND);
        return date.getTimeInMillis();
    }

    private static long roundDownMinute(long timeInMs) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMs);
        date.clear(Calendar.SECOND);
        date.clear(Calendar.MILLISECOND);
        return date.getTimeInMillis();
    }
}
