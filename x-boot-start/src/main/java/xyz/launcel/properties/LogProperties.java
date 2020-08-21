package xyz.launcel.properties;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("log")
public class LogProperties
{
    private File  file  = new File();
    private Level level = Level.INFO;

    @Getter
    @Setter
    public static class File
    {
        private String name = "catLog";
        private String path = "logs/";
    }
}
