package xyz.launcel.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Launcel [shay93@163.com]
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MD5
{

    private static String generateSalt(@NonNull String input)
    {
        byte[] code;
        try
        {
            code = MessageDigest.getInstance("md5").digest(input.getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
            code = input.getBytes();
        }
        return new BigInteger(code).abs().toString(32).toUpperCase();
    }

    public static String generate(@NonNull String input, String salt)
    {
        if (StringUtils.isBlank(salt))
        {
            salt = "";
        }
        return generateSalt(salt + generateSalt(input));
    }
}
