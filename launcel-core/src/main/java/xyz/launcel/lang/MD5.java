package xyz.launcel.lang;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Launcel [shay93@163.com]
 */
public class MD5 {
    
    private MD5() {
    }
    
    private static String generateSalt(String input) {
        byte[] code;
        try { code = MessageDigest.getInstance("md5").digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) { code = input.getBytes(); }
        BigInteger bi = new BigInteger(code);
        return bi.abs().toString(32).toUpperCase();
    }
    
    public static String generate(String input, String salt) {
        if (StringUtils.isBlank(salt)) { salt = ""; }
        return generateSalt(salt + generateSalt(input));
    }
}
