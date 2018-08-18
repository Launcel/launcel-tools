//package xyz.launcel.session.web.http;
//
//import org.springframework.session.Session;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.HttpSessionManager;
//import org.springframework.session.web.http.MultiHttpSessionStrategy;
//import org.springframework.util.Assert;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponseWrapper;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.StringTokenizer;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class PrimyCookieHttpSessionStrategy implements MultiHttpSessionStrategy, HttpSessionManager
//{
//
//    private String sessionParam = "_s";
//
//    private CookieSerializer cookieSerializer = new PrimyDefaultCookieSerializer();
//
//    private String deserializationDelimiter = " ";
//
//    private String serializationDelimiter = " ";
//
//    public String getRequestedSessionId(HttpServletRequest request)
//    {
//        Map<String, String> sessionIds   = getSessionIds(request);
//        String              sessionAlias = getCurrentSessionAlias(request);
//        return sessionIds.get(sessionAlias);
//    }
//
//    public String getCurrentSessionAlias(HttpServletRequest request)
//    {
//        if (this.sessionParam == null)
//        {
//            return "0";
//        }
//        String u = request.getParameter(this.sessionParam);
//        if (u == null)
//        {
//            return "0";
//        }
//        if (!Pattern.compile("^[\\w-]{1,50}$").matcher(u).matches())
//        {
//            return "0";
//        }
//        return u;
//    }
//
//    public String getNewSessionAlias(HttpServletRequest request)
//    {
//        Set<String> sessionAliases = getSessionIds(request).keySet();
//        if (sessionAliases.isEmpty())
//        {
//            return "0";
//        }
//        long lastAlias = Long.decode("0");
//        for (String alias : sessionAliases)
//        {
//            long selectedAlias = safeParse(alias);
//            if (selectedAlias > lastAlias)
//            {
//                lastAlias = selectedAlias;
//            }
//        }
//        return Long.toHexString(lastAlias + 1);
//    }
//
//    private long safeParse(String hex)
//    {
//        try
//        {
//            return Long.decode("0x" + hex);
//        }
//        catch (NumberFormatException notNumber)
//        {
//            return 0;
//        }
//    }
//
//    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response)
//    {
//        Set<String> sessionIdsWritten = getSessionIdsWritten(request);
//        if (sessionIdsWritten.contains(session.getId()))
//        {
//            return;
//        }
//        sessionIdsWritten.add(session.getId());
//
//        Map<String, String> sessionIds   = getSessionIds(request);
//        String              sessionAlias = getCurrentSessionAlias(request);
//        sessionIds.put(sessionAlias, session.getId());
//
//        String cookieValue = createSessionCookieValue(sessionIds);
//        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
//    }
//
//    private Set<String> getSessionIdsWritten(HttpServletRequest request)
//    {
//        String SESSION_IDS_WRITTEN_ATTR = "xyz.launcel.session.web.http.PrimyCookieHttpSessionStrategy.SESSIONS_WRITTEN_ATTR";
//        @SuppressWarnings("unchecked")
//        Set<String> sessionsWritten = (Set<String>) request.getAttribute(SESSION_IDS_WRITTEN_ATTR);
//        if (sessionsWritten == null)
//        {
//            sessionsWritten = new HashSet<>();
//            request.setAttribute(SESSION_IDS_WRITTEN_ATTR, sessionsWritten);
//        }
//        return sessionsWritten;
//    }
//
//    private String createSessionCookieValue(Map<String, String> sessionIds)
//    {
//        if (sessionIds.isEmpty())
//        {
//            return "";
//        }
//        if (sessionIds.size() == 1 && sessionIds.keySet().contains("0"))
//        {
//            return sessionIds.values().iterator().next();
//        }
//
//        StringBuilder buffer = new StringBuilder();
//        for (Map.Entry<String, String> entry : sessionIds.entrySet())
//        {
//            String alias = entry.getKey();
//            String id    = entry.getValue();
//
//            buffer.append(alias);
//            buffer.append(this.serializationDelimiter);
//            buffer.append(id);
//            buffer.append(this.serializationDelimiter);
//        }
//        buffer.deleteCharAt(buffer.length() - 1);
//        return buffer.toString();
//    }
//
//    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response)
//    {
//        Map<String, String> sessionIds     = getSessionIds(request);
//        String              requestedAlias = getCurrentSessionAlias(request);
//        sessionIds.remove(requestedAlias);
//
//        String cookieValue = createSessionCookieValue(sessionIds);
//        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
//    }
//
//    public void setSessionAliasParamName(String sessionAliasParamName)
//    {
//        this.sessionParam = sessionAliasParamName;
//    }
//
//    public void setCookieSerializer(CookieSerializer cookieSerializer)
//    {
//        Assert.notNull(cookieSerializer, "cookieSerializer cannot be null");
//        this.cookieSerializer = cookieSerializer;
//    }
//
//    @Deprecated
//    public void setCookieName(String cookieName)
//    {
//        PrimyDefaultCookieSerializer serializer = new PrimyDefaultCookieSerializer();
//        serializer.setCookieName(cookieName);
//        this.cookieSerializer = serializer;
//    }
//
//    public void setDeserializationDelimiter(String delimiter)
//    {
//        this.deserializationDelimiter = delimiter;
//    }
//
//    public void setSerializationDelimiter(String delimiter)
//    {
//        this.serializationDelimiter = delimiter;
//    }
//
//    public Map<String, String> getSessionIds(HttpServletRequest request)
//    {
//        List<String>        cookieValues       = this.cookieSerializer.readCookieValues(request);
//        String              sessionCookieValue = cookieValues.isEmpty() ? "" : cookieValues.iterator().next();
//        Map<String, String> result             = new LinkedHashMap<>();
//        StringTokenizer     tokens             = new StringTokenizer(sessionCookieValue, this.deserializationDelimiter);
//        if (tokens.countTokens() == 1)
//        {
//            result.put("0", tokens.nextToken());
//            return result;
//        }
//        while (tokens.hasMoreTokens())
//        {
//            String alias = tokens.nextToken();
//            if (!tokens.hasMoreTokens())
//            {
//                break;
//            }
//            String id = tokens.nextToken();
//            result.put(alias, id);
//        }
//        return result;
//    }
//
//    public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response)
//    {
//        request.setAttribute(HttpSessionManager.class.getName(), this);
//        return request;
//    }
//
//    public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response)
//    {
//        return new PrimyCookieHttpSessionStrategy.MultiSessionHttpServletResponse(response, request);
//    }
//
//    public String encodeURL(String url, String sessionAlias)
//    {
//        String  encodedSessionAlias = urlEncode(sessionAlias);
//        int     queryStart          = url.indexOf("?");
//        boolean isDefaultAlias      = "0".equals(encodedSessionAlias);
//        if (queryStart < 0)
//        {
//            return isDefaultAlias ? url : url + "?" + this.sessionParam + "=" + encodedSessionAlias;
//        }
//        String path        = url.substring(0, queryStart);
//        String query       = url.substring(queryStart + 1, url.length());
//        String replacement = isDefaultAlias ? "" : "$1" + encodedSessionAlias;
//        query = query.replaceFirst("((^|&)" + this.sessionParam + "=)([^&]+)?", replacement);
//        String sessionParamReplacement = String.format("%s=%s", this.sessionParam, encodedSessionAlias);
//
//        if (!isDefaultAlias && !query.contains(sessionParamReplacement) && url.endsWith(query))
//        {
//            // no existing alias
//            if (!(query.endsWith("&") || query.length() == 0))
//            {
//                query += "&";
//            }
//            query += sessionParamReplacement;
//        }
//
//        return path + "?" + query;
//    }
//
//    private String urlEncode(String value)
//    {
//        try
//        {
//            return URLEncoder.encode(value, "UTF-8");
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    class MultiSessionHttpServletResponse extends HttpServletResponseWrapper
//    {
//        private final HttpServletRequest request;
//
//        MultiSessionHttpServletResponse(HttpServletResponse response, HttpServletRequest request)
//        {
//            super(response);
//            this.request = request;
//        }
//
//        private String getCurrentSessionAliasFromUrl(String url)
//        {
//            String currentSessionAliasFromUrl = null;
//            int    queryStart                 = url.indexOf("?");
//
//            if (queryStart >= 0)
//            {
//                String  query   = url.substring(queryStart + 1);
//                Matcher matcher = Pattern.compile(String.format("%s=([^&]+)", sessionParam)).matcher(query);
//
//                if (matcher.find())
//                {
//                    currentSessionAliasFromUrl = matcher.group(1);
//                }
//            }
//
//            return currentSessionAliasFromUrl;
//        }
//
//        @Override
//        public String encodeRedirectURL(String url)
//        {
//            String encodedUrl                 = super.encodeRedirectURL(url);
//            String currentSessionAliasFromUrl = getCurrentSessionAliasFromUrl(encodedUrl);
//            String alias                      = (currentSessionAliasFromUrl != null) ? currentSessionAliasFromUrl : getCurrentSessionAlias(this.request);
//
//            return PrimyCookieHttpSessionStrategy.this.encodeURL(encodedUrl, alias);
//        }
//
//        @Override
//        public String encodeURL(String url)
//        {
//            String encodedUrl                 = super.encodeURL(url);
//            String currentSessionAliasFromUrl = getCurrentSessionAliasFromUrl(encodedUrl);
//            String alias                      = (currentSessionAliasFromUrl != null) ? currentSessionAliasFromUrl : getCurrentSessionAlias(this.request);
//
//            return PrimyCookieHttpSessionStrategy.this.encodeURL(encodedUrl, alias);
//        }
//    }
//}
