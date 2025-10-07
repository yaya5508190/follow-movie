package com.yx.nas.tool.plugins;


import org.babyfish.jimmer.client.EnableImplicitApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(name = "plugin.standalone", havingValue = "true")
@SpringBootApplication(scanBasePackages = "com.yx")
@EnableImplicitApi
public class MTeamMediaResourceApplication {
    public static void main(String[] args) {
        System.setProperty("plugin.standalone", "true");
        SpringApplication.run(MTeamMediaResourceApplication.class, args);
    }
}
