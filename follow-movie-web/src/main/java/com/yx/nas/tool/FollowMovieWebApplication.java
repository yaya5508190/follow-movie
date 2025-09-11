package com.yx.nas.tool;

import com.plugin.annotation.EnablePluginLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePluginLoader
public class
FollowMovieWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FollowMovieWebApplication.class, args);
    }

}
