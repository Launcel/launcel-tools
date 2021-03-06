package xyz.launcel.utils;

import lombok.NonNull;
import lombok.var;

import java.nio.charset.StandardCharsets;

public interface Base64
{
    static String encode(@NonNull String string)
    {
        return java.util.Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    static String decode(@NonNull String string)
    {
        var bytes = java.util.Base64.getDecoder().decode(string);
        if (bytes != null && bytes.length > 0)
        {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

}
