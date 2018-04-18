package xyz.launcel.lang;

import java.io.UnsupportedEncodingException;

public interface Base64 {
    
    static String encode(String string) {
        try {
            return java.util.Base64.getEncoder().encodeToString(string.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    
    static String decode(String string) {
        byte[] bytes = java.util.Base64.getDecoder().decode(string);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    
}
