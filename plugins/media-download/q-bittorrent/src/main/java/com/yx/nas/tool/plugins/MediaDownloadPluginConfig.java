package com.yx.nas.tool.plugins;

import api.PluginComponent;
import api.PluginConfig;
import com.google.auto.service.AutoService;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@AutoService(PluginConfig.class)
@Configuration
public class MediaDownloadPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "q-bittorrent";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public String desc() {
        return "";
    }

    @Override
    public String pluginId() {
        return "cc659cb6-9d35-4eef-bb08-4602eab88671";
    }

    @Override
    public List<PluginComponent> pluginComponents() {
        return List.of(
                new PluginComponent(pluginId(), "mediaResourceDownloaderSetting", "Q-Bittorrent", "App-Setting", "Q-Bittorrent下载器设置")
        );
    }
}
