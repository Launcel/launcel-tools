package xyz.launcel.utils.json.builder;


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
    private static Gson        gson        = null;
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

    public static Gson create()
    {
        if (gson == null)
        {
            synchronized (DefaultGsonBuilder.class)
            {
                if (gson == null)
                {
                    gson = gsonBuilder.create();
                }
            }
        }
        return gson;
    }

    private void setGsonBuilder()
    {
        if (floatingPointValue)
        {
            gsonBuilder.serializeSpecialFloatingPointValues();
        }
        if (formatPrint)
        {
            gsonBuilder.setPrettyPrinting();
        }
        if (serializeNull)
        {
            gsonBuilder.serializeNulls();
        }

        switch (dateFormat)
        {
            case LONG:
                gsonBuilder.setDateFormat(java.text.DateFormat.LONG);
                break;
            case SHORT_STRING:
                gsonBuilder.setDateFormat("yyyy-MM-dd");
                break;
            default:
                gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
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

        public void build()
        {
            new DefaultGsonBuilder(dateFormat, floatingPointValues, formatPrint, serializeNull, version);
        }

    }
}

