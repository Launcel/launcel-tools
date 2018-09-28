package xyz.launcel.lang;

import lombok.NonNull;

import java.nio.charset.StandardCharsets;

public interface Base64
{
    static String encode(@NonNull String string)
    {
        return java.util.Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    static String decode(@NonNull String string)
    {
        byte[] bytes = java.util.Base64.getDecoder().decode(string);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
