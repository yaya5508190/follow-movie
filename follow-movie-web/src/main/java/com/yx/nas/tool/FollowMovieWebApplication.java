package com.yx.nas.tool;

import com.plugin.annotation.EnablePluginLoader;
import org.babyfish.jimmer.client.EnableImplicitApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.yx.nas")
@EnablePluginLoader
@EnableImplicitApi
public class FollowMovieWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FollowMovieWebApplication.class, args);
    }
}
