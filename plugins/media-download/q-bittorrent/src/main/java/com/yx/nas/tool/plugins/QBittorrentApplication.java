package com.yx.nas.tool.plugins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

@ConditionalOnProperty(name = "plugin.standalone", havingValue = "true")
@SpringBootApplication(scanBasePackages = "com.yx")
public class QBittorrentApplication {
    public static void main(String[] args) {
        System.setProperty("plugin.standalone", "true");
        SpringApplication.run(QBittorrentApplication.class, args);
    }
}
