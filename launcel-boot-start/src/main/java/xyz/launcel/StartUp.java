package xyz.launcel;

import lombok.var;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
        var cac  = new SpringApplicationBuilder(appClass).web(WebApplicationType.SERVLET).bannerMode(Banner.Mode.OFF).run(args);
        var env  = cac.getEnvironment();
        var port = StringUtils.isEmpty(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port");

        String log = "app port is : \t{} \n\t\turl  is : \thttp://localhost:{}";
        try
        {
            var addr = InetAddress.getLocalHost();
            var ip   = addr.getHostAddress();
            log += "\n\treal url is : \thttp://{}";
            RootLogger.error(log, port, port, ip + ":" + port);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return env;
    }
}
