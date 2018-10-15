package xyz.launcel.json.builder;

public enum DateFormat
{
    LONG, SHORT_STRING, LONG_STRING;

    public static DateFormat getByName(String name)
    {
        for (DateFormat df : values())
        {
            if (df.name().equalsIgnoreCase(name))
            {
                return df;
            }
        }
        return LONG_STRING;
    }
}