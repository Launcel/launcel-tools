package xyz.launcel.lang;

import xyz.launcel.log.BaseLogger;

import java.io.UnsupportedEncodingException;

public class Base64 extends BaseLogger {

    public static String encode(String string) {
        try {
            return java.util.Base64.getEncoder().encodeToString(string.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            Info(">>> Base64 加密失败");
            return null;
        }
    }

    public static String decode(String string) {
        byte[] bytes = java.util.Base64.getDecoder().decode(string);
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Info(">>> Base64解密失败");
            return null;
        }
    }

}
