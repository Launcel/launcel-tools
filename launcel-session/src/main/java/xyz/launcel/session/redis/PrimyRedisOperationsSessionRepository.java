package xyz.launcel.session.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.util.Assert;
import xyz.launcel.session.MapSession;
import xyz.launcel.support.serializer.GsonRedisSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PrimyRedisOperationsSessionRepository implements FindByIndexNameSessionRepository<PrimyRedisOperationsSessionRepository.RedisSession>, MessageListener
{
    private static final Log logger = LogFactory.getLog(PrimyRedisOperationsSessionRepository.class);


    private static PrincipalNameResolver PRINCIPAL_NAME_RESOLVER = new PrincipalNameResolver();


    //    private static final String LAST_ACCESSED_ATTR = "lastAccessedTime";

    //    private static final String SESSION_ATTR_PREFIX = "sessionAttr:";

    private String keyPrefix = "session:";

    private final RedisOperations<String, Object> sessionRedisOperations;

    private final PrimyRedisSessionExpirationPolicy expirationPolicy;

    private RedisSerializer<Map> defaultSerializer = new GsonRedisSerializer<>(Map.class);
    //    private RedisSerializer<Map> defaultSerializer = new FastJsonRedisSerializer<>(Map.class);

    private ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher()
    {
        public void publishEvent(ApplicationEvent event)
        {
        }

        public void publishEvent(Object event)
        {
        }
    };

    private Integer defaultMaxInactiveInterval;


    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;


    public PrimyRedisOperationsSessionRepository(RedisOperations<String, Object> sessionRedisOperations)
    {
        Assert.notNull(sessionRedisOperations, "sessionRedisOperations cannot be null");
        this.sessionRedisOperations = sessionRedisOperations;
        this.expirationPolicy = new PrimyRedisSessionExpirationPolicy(sessionRedisOperations, this);
    }

    public PrimyRedisOperationsSessionRepository(RedisConnectionFactory redisConnectionFactory)
    {
        this(createDefaultTemplate(redisConnectionFactory));
    }


    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        Assert.notNull(applicationEventPublisher, "applicationEventPublisher cannot be null");
        this.eventPublisher = applicationEventPublisher;
    }


    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval)
    {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }


    public void setDefaultSerializer(RedisSerializer<Map> defaultSerializer)
    {
        Assert.notNull(defaultSerializer, "defaultSerializer cannot be null");
        this.defaultSerializer = defaultSerializer;
    }


    public void setRedisFlushMode(RedisFlushMode redisFlushMode)
    {
        Assert.notNull(redisFlushMode, "redisFlushMode cannot be null");
        this.redisFlushMode = redisFlushMode;
    }

    public void save(RedisSession session)
    {
        session.saveDelta();
        if (session.isNew())
        {
            String sessionCreatedKey = getSessionCreatedChannel(session.getId());
            this.sessionRedisOperations.convertAndSend(sessionCreatedKey, session.delta);
            session.setNew();
        }
    }

    @Scheduled(cron = "${spring.session.cleanup.cron.expression:0 * * * * *}")
    public void cleanupExpiredSessions()
    {
        this.expirationPolicy.cleanExpiredSessions();
    }

    public RedisSession getSession(String id)
    {
        return getSession(id, false);
    }

    public Map<String, RedisSession> findByIndexNameAndIndexValue(String indexName, String indexValue)
    {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName))
        {
            return Collections.emptyMap();
        }
        String                    principalKey = getPrincipalKey(indexValue);
        Set<Object>               sessionIds   = this.sessionRedisOperations.boundSetOps(principalKey).members();
        Map<String, RedisSession> sessions     = new HashMap<>(sessionIds.size());
        for (Object id : sessionIds)
        {
            RedisSession session = getSession((String) id);
            if (session != null)
            {
                sessions.put(session.getId(), session);
            }
        }
        return sessions;
    }


    private RedisSession getSession(String id, boolean allowExpired)
    {
        Map<String, Object> entries = getSessionBoundHashOperations(id).entries();
        if (entries.isEmpty())
        {
            return null;
        }
        MapSession loaded = loadSession(id, entries);
        if (!allowExpired && loaded.isExpired())
        {
            return null;
        }
        RedisSession result = new RedisSession(loaded);
        result.originalLastAccessTime = loaded.getLastAccessedTime();
        return result;
    }

    private MapSession loadSession(String id, Map<String, Object> entries)
    {
        MapSession loaded = new MapSession(id);
        for (Map.Entry<String, Object> entry : entries.entrySet())
        {
            String key = entry.getKey();
            if ("creationTime".equals(key))
            {
                loaded.setCreationTime(((Double) entry.getValue()).longValue());
            }
            else if ("maxInactiveInterval".equals(key))
            {
                loaded.setMaxInactiveIntervalInSeconds(((Double) entry.getValue()).intValue());
            }
            else if ("lastAccessedTime".equals(key))
            {
                loaded.setLastAccessedTime(((Double) entry.getValue()).longValue());
            }
            else if (key.startsWith("sessionAttr:"))
            {
                loaded.setAttribute(key.substring("sessionAttr:".length()), entry.getValue());
            }
        }
        return loaded;
    }

    public void delete(String sessionId)
    {
        RedisSession session = getSession(sessionId, true);
        if (session == null)
        {
            return;
        }

        cleanupPrincipalIndex(session);
        this.expirationPolicy.onDelete(session);

        String expireKey = getExpiredKey(session.getId());
        this.sessionRedisOperations.delete(expireKey);

        session.setMaxInactiveIntervalInSeconds(0);
        save(session);
    }

    @Override
    public RedisSession createSession()
    {
        RedisSession redisSession = new RedisSession();
        if (this.defaultMaxInactiveInterval != null)
        {
            redisSession.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return redisSession;
    }

    @SuppressWarnings("unchecked")
    public void onMessage(Message message, byte[] pattern)
    {
        byte[] messageChannel = message.getChannel();
        byte[] messageBody    = message.getBody();
        if (messageChannel == null || messageBody == null)
        {
            return;
        }

        String channel = new String(messageChannel);

        if (channel.startsWith(getSessionCreatedChannelPrefix()))
        {
            // TODO: is this thread safe?
            Map<String, Object> loaded = this.defaultSerializer.deserialize(message.getBody());
            handleCreated(loaded, channel);
            return;
        }

        String body = new String(messageBody);
        if (!body.startsWith(getExpiredKeyPrefix()))
        {
            return;
        }

        boolean isDeleted = channel.endsWith(":del");
        if (isDeleted || channel.endsWith(":expired"))
        {
            int    beginIndex = body.lastIndexOf(":") + 1;
            int    endIndex   = body.length();
            String sessionId  = body.substring(beginIndex, endIndex);

            RedisSession session = getSession(sessionId, true);

            if (logger.isDebugEnabled())
            {
                logger.debug("Publishing SessionDestroyedEvent for session " + sessionId);
            }

            cleanupPrincipalIndex(session);

            if (isDeleted)
            {
                handleDeleted(sessionId, session);
            }
            else
            {
                handleExpired(sessionId, session);
            }
        }
    }

    private void cleanupPrincipalIndex(RedisSession session)
    {
        if (session == null)
        {
            return;
        }
        String sessionId = session.getId();
        String principal = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(session);
        if (principal != null)
        {
            this.sessionRedisOperations.boundSetOps(getPrincipalKey(principal)).remove(sessionId);
        }
    }

    private void handleCreated(Map<String, Object> loaded, String channel)
    {
        String          id      = channel.substring(channel.lastIndexOf(":") + 1);
        ExpiringSession session = loadSession(id, loaded);
        publishEvent(new SessionCreatedEvent(this, session));
    }

    private void handleDeleted(String sessionId, RedisSession session)
    {
        if (session == null)
        {
            publishEvent(new SessionDeletedEvent(this, sessionId));
        }
        else
        {
            publishEvent(new SessionDeletedEvent(this, session));
        }
    }

    private void handleExpired(String sessionId, RedisSession session)
    {
        if (session == null)
        {
            publishEvent(new SessionExpiredEvent(this, sessionId));
        }
        else
        {
            publishEvent(new SessionExpiredEvent(this, session));
        }
    }

    private void publishEvent(ApplicationEvent event)
    {
        try
        {
            this.eventPublisher.publishEvent(event);
        }
        catch (Throwable ex)
        {
            logger.error("Error publishing " + event + ".", ex);
        }
    }

    public void setRedisKeyNamespace(String namespace)
    {
        this.keyPrefix += namespace + ":";
    }

    String getSessionKey(String sessionId)
    {
        return this.keyPrefix + sessionId + ":sessionId:" + sessionId;
    }

    private String getPrincipalKey(String principalName)
    {
        return this.keyPrefix + "index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":" + principalName;
    }

    //    String getExpirationsKey(String sessionId, long expiration) {
    //        return keyPrefix + sessionId + ":expirations:" + expiration;
    //    }

    private String getExpiredKey(String sessionId)
    {
        return keyPrefix + sessionId + ":" + sessionId;
    }

    private String getSessionCreatedChannel(String sessionId)
    {
        return getSessionCreatedChannelPrefix() + sessionId;
    }

    private String getExpiredKeyPrefix()
    {
        return this.keyPrefix;
    }

    public String getSessionCreatedChannelPrefix()
    {
        return this.keyPrefix + "event:created:";
    }

    private static String getSessionAttrNameKey(String attributeName)
    {
        return "sessionAttr:" + attributeName;
    }

    private static RedisTemplate<String, Object> createDefaultTemplate(RedisConnectionFactory connectionFactory)
    {
        Assert.notNull(connectionFactory, "connectionFactory cannot be null");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        GsonRedisSerializer<Object> serializer = new GsonRedisSerializer<>(Object.class);
        //        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setDefaultSerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }


    final class RedisSession implements ExpiringSession
    {
        private final MapSession cached;
        private       Long       originalLastAccessTime;
        private Map<String, Object> delta = new HashMap<>();
        private boolean isNew;
        private String  originalPrincipalName;


        RedisSession()
        {
            this(new MapSession());
            this.delta.put("creationTime", getCreationTime());
            this.delta.put("maxInactiveInterval", getMaxInactiveIntervalInSeconds());
            this.delta.put("lastAccessedTime", getLastAccessedTime());
            this.isNew = true;
            this.flushImmediateIfNecessary();
        }

        RedisSession(MapSession cached)
        {
            Assert.notNull(cached, "MapSession cannot be null");
            this.cached = cached;
            this.originalPrincipalName = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(this);
        }

        //        private void setNew(boolean isNew) {
        private void setNew()
        {
            this.isNew = false;
        }

        public void setLastAccessedTime(long lastAccessedTime)
        {
            this.cached.setLastAccessedTime(lastAccessedTime);
            this.putAndFlush("lastAccessedTime", getLastAccessedTime());
        }

        public boolean isExpired()
        {
            return this.cached.isExpired();
        }

        private boolean isNew()
        {
            return this.isNew;
        }

        public long getCreationTime()
        {
            return this.cached.getCreationTime();
        }

        public String getId()
        {
            return this.cached.getId();
        }

        public long getLastAccessedTime()
        {
            return this.cached.getLastAccessedTime();
        }

        public void setMaxInactiveIntervalInSeconds(int interval)
        {
            this.cached.setMaxInactiveIntervalInSeconds(interval);
            this.putAndFlush("maxInactiveInterval", getMaxInactiveIntervalInSeconds());
        }

        public int getMaxInactiveIntervalInSeconds()
        {
            return this.cached.getMaxInactiveIntervalInSeconds();
        }

        public <T> T getAttribute(String attributeName)
        {
            return this.cached.getAttribute(attributeName);
        }

        public Set<String> getAttributeNames()
        {
            return this.cached.getAttributeNames();
        }

        public void setAttribute(String attributeName, Object attributeValue)
        {
            this.cached.setAttribute(attributeName, attributeValue);
            this.putAndFlush(getSessionAttrNameKey(attributeName), attributeValue);
        }

        public void removeAttribute(String attributeName)
        {
            this.cached.removeAttribute(attributeName);
            this.putAndFlush(getSessionAttrNameKey(attributeName), null);
        }

        private void flushImmediateIfNecessary()
        {
            if (redisFlushMode == RedisFlushMode.IMMEDIATE)
            {
                saveDelta();
            }
        }

        private void putAndFlush(String a, Object v)
        {
            this.delta.put(a, v);
            this.flushImmediateIfNecessary();
        }

        private void saveDelta()
        {
            if (this.delta.isEmpty())
            {
                return;
            }
            String sessionId = getId();
            getSessionBoundHashOperations(sessionId).putAll(this.delta);
            String principalSessionKey         = getSessionAttrNameKey(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
            String securityPrincipalSessionKey = getSessionAttrNameKey("SPRING_SECURITY_CONTEXT");
            if (this.delta.containsKey(principalSessionKey) || this.delta.containsKey(securityPrincipalSessionKey))
            {
                if (this.originalPrincipalName != null)
                {
                    String originalPrincipalRedisKey = getPrincipalKey(this.originalPrincipalName);
                    sessionRedisOperations.boundSetOps(originalPrincipalRedisKey).remove(sessionId);
                }
                String principal = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(this);
                this.originalPrincipalName = principal;
                if (principal != null)
                {
                    String principalRedisKey = getPrincipalKey(principal);
                    sessionRedisOperations.boundSetOps(principalRedisKey).add(sessionId);
                }
            }

            this.delta = new HashMap<>(this.delta.size());

            Long originalExpiration = this.originalLastAccessTime == null ? null : this.originalLastAccessTime + TimeUnit.SECONDS.toMillis(getMaxInactiveIntervalInSeconds());
            expirationPolicy.onExpirationUpdated(originalExpiration, this);
        }
    }

    static class PrincipalNameResolver
    {
        private SpelExpressionParser parser = new SpelExpressionParser();

        public String resolvePrincipal(Session session)
        {
            String principalName = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
            if (principalName != null)
            {
                return principalName;
            }
            Object authentication = session.getAttribute("SPRING_SECURITY_CONTEXT");
            if (authentication != null)
            {
                Expression expression = this.parser.parseExpression("authentication?.name");
                return expression.getValue(authentication, String.class);
            }
            return null;
        }

    }

    private BoundHashOperations<String, String, Object> getSessionBoundHashOperations(String sessionId)
    {
        String key = getSessionKey(sessionId);
        return this.sessionRedisOperations.boundHashOps(key);
    }

    private BoundValueOperations<String, Object> getSessionBoundValueOperations(String sessionId)
    {
        String key = getSessionKey(sessionId);
        return sessionRedisOperations.boundValueOps(key);
    }
}
