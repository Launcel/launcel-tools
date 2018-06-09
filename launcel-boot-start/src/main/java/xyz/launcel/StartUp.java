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
import xyz.launcel.log.RootLogger;

public interface StartUp
{


    static ConfigurableEnvironment run(String[] args, Class<?> appClass)
    {
        //        Logger            log = LoggerFactory.getLogger(appClass);
        SpringApplication app = new SpringApplication(appClass);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        //        log.error("\n------------------------------------\n\t\tapp port is : \t{}", env.getProperty("server.port") + "\n\t\turl is : \t" + "http://localhost:" + env.getProperty("server.port") + "\n------------------------------------");
        RootLogger.ERROR("\tapp port is : \t{}", env.getProperty("server.port") + "\n\t\turl is : \t" + "http://localhost:" + env.getProperty("server.port"));
        return env;
    }

}
