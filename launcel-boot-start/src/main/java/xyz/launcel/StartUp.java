/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.17
 * Version: 1.0
 */

package xyz.launcel;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import xyz.launcel.log.RootLogger;

public interface StartUp
{
    static ConfigurableEnvironment run(String[] args, Class<?> appClass)
    {
        SpringApplication app = new SpringApplication(appClass);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        String port = StringUtils.isEmpty(env.getProperty("server.port")) ? "8080" : env.getProperty("server.port");
        RootLogger.error("\tapp port is : \t{}",  port + "\n\t\turl is : \t" + "http://localhost:" + port);
        return env;
    }

}
