/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.17
 * Version: 1.0
 */

package xyz.launcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public interface StartUp {


    static ConfigurableEnvironment run(String[] args, Class<?> appClass) {
        Logger log = LoggerFactory.getLogger(StartUp.class);
        SpringApplication app = new SpringApplication(appClass);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableEnvironment env = app.run(args).getEnvironment();
        log.warn("\n------------------------------------\n\t\tapp port is : \t{}",
                env.getProperty("server.port") + "\n------------------------------------");
        return env;
    }

}
