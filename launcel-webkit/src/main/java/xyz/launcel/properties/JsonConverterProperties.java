package xyz.launcel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "web.json-converter")
public class JsonConverterProperties
{

    private Boolean enabled    = true;
    private String  dateFormat = "yyyy-MM-dd HH:mm:ss";

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }
}
