package xyz.launcel.json.builder;

import lombok.var;

public enum DateFormat
{
    LONG,
    SHORT_STRING,
    LONG_STRING;

    public static DateFormat getByName(String name)
    {
        for (var df : values())
        {
            if (df.name().equalsIgnoreCase(name))
            {
                return df;
            }
        }
        return LONG_STRING;
    }
}