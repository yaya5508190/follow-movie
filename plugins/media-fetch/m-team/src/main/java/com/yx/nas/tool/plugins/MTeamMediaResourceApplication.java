package com.yx.nas.tool.plugins;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yx")
@SpringBootApplication
public class MTeamMediaResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MTeamMediaResourceApplication.class, args);
    }
}
