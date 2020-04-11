package xyz.launcel.utils;

import lombok.NonNull;
import lombok.var;
import xyz.launcel.exception.ExceptionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public interface Base64
{
    static String encode(@NonNull String string)
    {
        return java.util.Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    static String decode(@NonNull String string)
    {
        var bytes = java.util.Base64.getDecoder().decode(string);
        if (Objects.isNull(bytes) || bytes.length < 1)
        {
            ExceptionFactory.error("0021");
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
