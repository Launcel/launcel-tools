package xyz.x.common.utils.json.builder;

import java.util.Arrays;

public enum DateFormat
{
    LONG,
    SHORT_STRING,
    LONG_STRING;

    public static DateFormat getByName(String name)
    {
        return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElse(LONG_STRING);
    }
}