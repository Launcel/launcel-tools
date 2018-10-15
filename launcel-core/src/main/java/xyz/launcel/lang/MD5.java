package xyz.launcel.lang;


import lombok.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Launcel [shay93@163.com]
 */
public class MD5
{

    private MD5() { }

    private static String generateSalt(@NonNull String input)
    {
        byte[] code;
        try
        {
            code = MessageDigest.getInstance("md5").digest(input.getBytes());
        }
        catch (NoSuchAlgorithmException e) { code = input.getBytes(); }
        var bi = new BigInteger(code);
        return bi.abs().toString(32).toUpperCase();
    }

    public static String generate(@NonNull String input, String salt)
    {
        if (StringUtils.isBlank(salt)) { salt = ""; }
        return generateSalt(salt + generateSalt(input));
    }
}
