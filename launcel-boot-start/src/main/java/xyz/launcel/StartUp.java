package xyz.launcel;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import xyz.launcel.log.RootLogger;

public interface StartUp
{
    static ConfigurableEnvironment run(@NonNull Class<?> appClass, @NonNull String[] args)
    {
        var cac  = new SpringApplicationBuilder(appClass).web(WebApplicationType.SERVLET).bannerMode(Banner.Mode.OFF).run(args);
        var env  = cac.getEnvironment();
        var port = StringUtils.isEmpty(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port");
        RootLogger.error("app port is : \t{}", port + "\n\t\turl is : \t" + "http://localhost:" + port);
        return env;
    }


}
