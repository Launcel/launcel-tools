package xyz.launcel;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import xyz.launcel.log.RootLogger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface StartUp
{
    static ConfigurableEnvironment run(@NonNull Class<?> appClass, @NonNull String[] args)
    {
        ConfigurableApplicationContext cac = new SpringApplicationBuilder(appClass).web(WebApplicationType.SERVLET)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        ConfigurableEnvironment env  = cac.getEnvironment();
        String                  port = StringUtils.isEmpty(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port");
        RootLogger.error("app port is : \t{}, \n\t\turl is : \thttp://localhost:", port, port);
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            String      ip   = addr.getHostAddress();
            RootLogger.error("app port is : \t{}, \n\t\turl is : \thttp://{}", port, ip + port);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return env;
    }
}
