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

    private static String md5(String input) {
        byte[] code;
        try {
            code = MessageDigest.getInstance("md5").digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            code = input.getBytes();
        }
        BigInteger bi = new BigInteger(code);
        return bi.abs().toString(32).toUpperCase();
    }

    public static String generateSalt(String input, String salt) {
        if (StringUtils.isBlank(salt))
            salt = "";
        return md5(salt + md5(input));
    }

    public static void main(String[] args) {
        System.out.println(md5("111"));
    }
}
