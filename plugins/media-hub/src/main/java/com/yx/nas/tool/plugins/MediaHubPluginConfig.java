package com.yx.nas.tool.plugins;

import api.PluginComponent;
import api.PluginConfig;
import com.google.auto.service.AutoService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@AutoService(PluginConfig.class)
@Configuration
@ComponentScan(basePackages = "com.yx.framework")
public class MediaHubPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "豆瓣推荐";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public String desc() {
        return "热门影片推荐";
    }

    @Override
    public String pluginId() {
        return "9d220be1-8f62-4819-8f00-03911dc00a95";
    }

    @Override
    public List<PluginComponent> pluginComponents() {
        return List.of(
                new PluginComponent(pluginId(), "menu", "影片推荐/豆瓣推荐", "App", "豆瓣热门影片推荐")
        );
    }
}
