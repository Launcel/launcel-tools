package xyz.launcel.config;

import xyz.launcel.json.Json;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.RootLogger;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityConfig {
    private static       Map<String, Set<String>> uris = new HashMap<>();
    
    
    public static Map<String, Set<String>> getUris() {
        return uris;
    }
    
    @SuppressWarnings({ "unchecked" })
    public static void setUris(Map<String, String[]> uriProp) {
        if (uriProp.isEmpty()) {return;}
        uriProp.forEach((key, value) -> uris.put(key, new HashSet<>(Arrays.asList(value))));
    }
    
    @SuppressWarnings("unchecked")
    public static boolean isTransit(String requestUriString, HttpSession session) {
        if (requestUriString.isEmpty()) { return true; }
        List<String> requestUris = (List<String>) StringUtils.spiltStream(requestUriString, "/").collect(Collectors.toList());
        if (CollectionUtils.isEmpty(requestUris)) { return true; }
        Set<String>          tempRoleUris = new HashSet<>(uris.keySet());
        Map<String, Integer> maxMatchMap  = temp(uris.keySet());
        for (int i = 0; i < requestUris.size(); i++) {
            Iterator<String> it = tempRoleUris.iterator();
            while (it.hasNext()) {
                String tempRoleUri = it.next();
                if (requestUriString.equals(tempRoleUri)) {
                    return validateRole(uris.get(tempRoleUri), session);
                }
                List<String> roleUris = (List<String>) StringUtils.spiltStream(tempRoleUri, "/").collect(Collectors.toList());
                if (null != roleUris.get(i)) {
                    int j = validateUri(requestUris.get(i), roleUris.get(i));
                    if (j == -1) {
                        it.remove();
                        maxMatchMap.remove(tempRoleUri);
                    } else if (j == 1) {
                        maxMatchMap.replace(tempRoleUri, maxMatchMap.get(tempRoleUri) + requestUris.size() - i);
                    }
                }
            }
        }
        return maxMatchMap.isEmpty() || validateRole(uris.get(getBestUriKey(maxMatchMap)), session);
    }
    
    private static Map<String, Integer> temp(Collection<String> c) {
        Map<String, Integer> tempMap = new HashMap<>();
        for (String aC : c) {
            tempMap.put(aC, 0);
        }
        return tempMap;
    }
    
    private static String getBestUriKey(Map<String, Integer> maxMatchMap) {
        if (RootLogger.isDebug()) {
            RootLogger.DEBUG("match : " + Json.toJson(maxMatchMap));
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(maxMatchMap.entrySet());
        list.sort((Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> o2.getValue().compareTo(o1.getValue()));
        if (RootLogger.isDebug()) {
            RootLogger.DEBUG("the match uri is : " + list.get(0).getKey() + " , match weight is : " + list.get(0).getValue());
        }
        return list.get(0).getKey();
    }
    
    private static int validateUri(String requestUri, String roleUri) {
        int flat = -1;
        if (requestUri.equals(roleUri)) {
            flat = 1;
        } else if (roleUri.equals("**")) {
            flat = 0;
        }
        return flat;
    }
    
    private static boolean validateRole(Set<String> roles, HttpSession session) {
        if (roles.contains("anon")) {
            return true;
        }
        if (session == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Set<String> userRoles = (Set<String>) session.getAttribute("role");
        for (String userRole : userRoles) {
            if (roles.contains(userRole)) {
                return true;
            }
        }
        return false;
    }
    
}
