package xyz.launcel.session;

import org.springframework.session.ExpiringSession;
import org.springframework.session.Session;
import xyz.launcel.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MapSession implements ExpiringSession, Serializable {

    private static final long serialVersionUID = -4926709993975115053L;

    private static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1200;

    private String id;
    private Map<String, Object> sessionAttrs = new HashMap<>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = this.creationTime;

    private int maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

    public MapSession() {
        this(StringUtils.getUUID());
    }

    public MapSession(String id) {
        this.id = id;
    }

    public MapSession(ExpiringSession session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        this.id = session.getId();
        this.sessionAttrs = new HashMap<>(session.getAttributeNames().size());
        for (String attrName : session.getAttributeNames()) {
            Object attrValue = session.getAttribute(attrName);
            this.sessionAttrs.put(attrName, attrValue);
        }
        this.lastAccessedTime = session.getLastAccessedTime();
        this.creationTime = session.getCreationTime();
        this.maxInactiveInterval = session.getMaxInactiveIntervalInSeconds();
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.id;
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public void setMaxInactiveIntervalInSeconds(int interval) {
        this.maxInactiveInterval = interval;
    }

    public int getMaxInactiveIntervalInSeconds() {
        return this.maxInactiveInterval;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

    private boolean isExpired(long now) {
        return this.maxInactiveInterval >= 0 && now - TimeUnit.SECONDS.toMillis(this.maxInactiveInterval) >= this.lastAccessedTime;
    }

    public <T> T getAttribute(String attributeName) {
        //noinspection unchecked
        return (T) this.sessionAttrs.get(attributeName);
    }

    public Set<String> getAttributeNames() {
        return this.sessionAttrs.keySet();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            this.sessionAttrs.put(attributeName, attributeValue);
        }
    }

    public void removeAttribute(String attributeName) {
        this.sessionAttrs.remove(attributeName);
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return obj instanceof Session && this.id.equals(((Session) obj).getId());
    }

    public int hashCode() {
        return this.id.hashCode();
    }

}
