package xyz.launcel;

import lombok.var;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import xyz.launcel.log.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author launcel
 */
public interface StartUp
{
    static ConfigurableEnvironment run(@NonNull Class<?> appClass, @NonNull String[] args)
    {
        return run(appClass, args, null, null);
    }

    static ConfigurableEnvironment run(@NonNull Class<?> appClass, @NonNull String[] args, WebApplicationType type, Banner.Mode bannerMode)
    {
        if (Objects.isNull(type))
        {
            type = WebApplicationType.SERVLET;
        }
        if (Objects.isNull(bannerMode))
        {
            bannerMode = Banner.Mode.OFF;
        }
        var cac          = new SpringApplicationBuilder(appClass).web(type).bannerMode(bannerMode).run(args);
        var env          = cac.getEnvironment();
        var portProperty = env.getProperty("server.port");
        var port         = StringUtils.isEmpty(portProperty) ? "8080" : portProperty;

        String log = "app port is : \t{} \n\t\turl  is : \thttp://localhost:{}";
        try
        {
            var addr = InetAddress.getLocalHost();
            var ip   = addr.getHostAddress();
            log += "\n\treal url is : \thttp://{}";
            Log.error(log, port, port, ip + ":" + port);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return env;
    }
}
