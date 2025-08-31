package com.yx.nas.tool.plugins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yx")
@SpringBootApplication
public class MediaHubPluginApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaHubPluginApplication.class, args);
    }

}
