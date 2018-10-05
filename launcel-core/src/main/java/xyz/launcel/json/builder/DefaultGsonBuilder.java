package xyz.launcel.json.builder;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class DefaultGsonBuilder
{
    private        DateFormat  dateFormat;
    private        boolean     floatingPointValue;
    private        boolean     formatPrint;
    private        boolean     serializeNull;
    private        Double      version;
    private static GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(),
            new JsonMapDeserializer());


    private DefaultGsonBuilder(DateFormat dateFormat, boolean floatingPointValue, boolean formatPrint, boolean serializeNull,
                               Double version)
    {
        this.dateFormat = dateFormat;
        this.floatingPointValue = floatingPointValue;
        this.formatPrint = formatPrint;
        this.serializeNull = serializeNull;
        this.version = version;
        setGsonBuilder();
    }

    public static DefaultBuilder builder()
    {
        return new DefaultBuilder();
    }

    private void setGsonBuilder()
    {
        if (floatingPointValue)
            gsonBuilder.serializeSpecialFloatingPointValues();
        if (formatPrint)
            gsonBuilder.setPrettyPrinting();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateFormat == DateFormat.LONG)
        {
            gsonBuilder.setDateFormat(java.text.DateFormat.LONG);
        }
        else if (dateFormat == DateFormat.SHORT_STRING)
        {
            gsonBuilder.setDateFormat("yyyy-MM-dd");
        }
        if (serializeNull)
        {
            gsonBuilder.serializeNulls();
        }
        if (version != null)
        {
            gsonBuilder.setVersion(version);
        }
    }

    public static GsonBuilder getGsonBuilder()
    {
        return gsonBuilder;
    }

    public Gson create()
    {
        return gsonBuilder.create();
    }

    public static class DefaultBuilder
    {
        private DateFormat dateFormat          = DateFormat.LONG_STRING;
        private boolean    floatingPointValues = true;
        private boolean    formatPrint         = true;
        private boolean    serializeNull       = true;
        private Double     version             = null;

        public DefaultBuilder dateFormat(DateFormat dateFormat)
        {
            this.dateFormat = dateFormat;
            return this;
        }

        public DefaultBuilder floatingPointValues(boolean floatingPointValues)
        {
            this.floatingPointValues = floatingPointValues;
            return this;
        }

        public DefaultBuilder formatPrint(boolean formatPrint)
        {
            this.formatPrint = formatPrint;
            return this;
        }

        public DefaultBuilder serializeNull(boolean serializeNull)
        {
            this.serializeNull = serializeNull;
            return this;
        }

        public DefaultBuilder version(Double version)
        {
            this.version = version;
            return this;
        }

        public DefaultGsonBuilder build()
        {
            return new DefaultGsonBuilder(dateFormat, floatingPointValues, formatPrint, serializeNull, version);
        }

    }

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

}
