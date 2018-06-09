package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web.cors")
public class CorsProperties
{

    private Boolean  enabled        = false;
    private String   pathPattern    = "/api/**";
    private String[] methods        = {"GET", "POST", "DELETE", "PUT"};
    private Long     maxAge         = 1800L;
    private String[] allowedOrigins = {"*"};

    /**
     * Enable cross-origin request handling for the specified path pattern.
     * <p>Exact path mapping URIs (such as {@code "/admin"}) are supported as
     * well as Ant-style path patterns (such as {@code "/admin/**"}).
     * <p>By default, all origins, all headers, credentials and {@code GET},
     * {@code HEAD}, and {@code POST} methods are allowed, and the max age
     * is set to 30 minutes.
     *
     * @param pathPattern the path pattern to enable CORS handling for
     *
     * @return CorsRegistration the corresponding registration object,
     * allowing for further fine-tuning
     */
    public String getPathPattern()
    {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern)
    {
        this.pathPattern = pathPattern;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String[] getMethods()
    {
        return methods;
    }

    public void setMethods(String[] methods)
    {
        this.methods = methods;
    }

    public Long getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Long maxAge)
    {
        this.maxAge = maxAge;
    }

    /**
     * Set the origins to allow, e.g. {@code "http://domain1.com"}.
     * <p>The special value {@code "*"} allows all domains.
     * <p>By default, all origins are allowed.
     */
    public String[] getAllowedOrigins()
    {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins)
    {
        this.allowedOrigins = allowedOrigins;
    }
}
