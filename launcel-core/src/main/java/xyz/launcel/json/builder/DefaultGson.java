package xyz.launcel.json.builder;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class DefaultGson
{
    private        DateFormat  dateFormat          = DateFormat.LONG_STRING;
    private        boolean     floatingPointValues = true;
    private        boolean     formatPrint         = true;
    private        boolean     serializeNull       = true;
    private        Double      version             = null;
    private static GsonBuilder gsonBuilder         = new GsonBuilder().registerTypeAdapter(
            new TypeToken<Map<String, Object>>() {}.getType(), new JsonMapDeserializer());


    public DefaultGson(DateFormat dateFormat, boolean floatingPointValues, boolean formatPrint, boolean serializeNull, Double version)
    {
        this.dateFormat = dateFormat;
        this.floatingPointValues = floatingPointValues;
        this.formatPrint = formatPrint;
        this.serializeNull = serializeNull;
        this.version = version;
        setGsonBuilder();
    }

    public static DefaultGsonBuilder builder()
    {
        return new DefaultGsonBuilder();
    }

    private void setGsonBuilder()
    {
        if (floatingPointValues)
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

    public static class DefaultGsonBuilder
    {
        private DateFormat dateFormat = DateFormat.LONG_STRING;

        private boolean floatingPointValues;

        private boolean formatPrint;

        private boolean serializeNull = true;
        private Double  version;

        public DefaultGsonBuilder dateFormat(DateFormat dateFormat)
        {
            this.dateFormat = dateFormat;
            return this;
        }

        public DefaultGsonBuilder floatingPointValues(boolean floatingPointValues)
        {
            this.floatingPointValues = floatingPointValues;
            return this;
        }

        public DefaultGsonBuilder formatPrint(boolean formatPrint)
        {
            this.formatPrint = formatPrint;
            return this;
        }

        public DefaultGsonBuilder serializeNull(boolean serializeNull)
        {
            this.serializeNull = serializeNull;
            return this;
        }

        public DefaultGsonBuilder version(Double version)
        {
            this.version = version;
            return this;
        }

        public DefaultGson build()
        {
            return new DefaultGson(dateFormat, floatingPointValues, formatPrint, serializeNull, version);
        }

    }

    public enum DateFormat
    {
        LONG, SHORT_STRING, LONG_STRING,
    }

}
