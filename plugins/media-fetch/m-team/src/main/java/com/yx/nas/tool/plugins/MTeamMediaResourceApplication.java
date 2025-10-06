package com.yx.nas.tool.plugins;


import org.babyfish.jimmer.client.EnableImplicitApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yx")
@EnableImplicitApi
@SpringBootApplication
public class MTeamMediaResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MTeamMediaResourceApplication.class, args);
    }
}
