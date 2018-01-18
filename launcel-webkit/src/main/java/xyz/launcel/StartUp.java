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
import xyz.launcel.log.BaseLogger;

public class StartUp extends BaseLogger {

    public static void run(String[] args, Class<?> appClass) {
        SpringApplication app = new SpringApplication(appClass);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        Warn("\n------------------------------------\n\t\tapp port is : \t{}",
                env.getProperty("server.port") + "\n------------------------------------");
    }

}
