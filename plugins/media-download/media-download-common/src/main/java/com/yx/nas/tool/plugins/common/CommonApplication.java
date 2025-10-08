package com.yx.nas.tool.plugins.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(name = "plugin.standalone", havingValue = "true")
@SpringBootApplication(scanBasePackages = "com.yx")
public class CommonApplication {
    public static void main(String[] args) {
        System.setProperty("plugin.standalone", "true");
        SpringApplication.run(CommonApplication.class, args);
    }
}
