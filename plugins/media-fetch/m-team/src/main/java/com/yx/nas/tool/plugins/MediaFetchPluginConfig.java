package com.yx.nas.tool.plugins;

import api.PluginComponent;
import api.PluginConfig;
import com.google.auto.service.AutoService;
import org.springframework.stereotype.Component;

import java.util.List;

@AutoService(PluginConfig.class)
@Component
public class MediaFetchPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "m-team";
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
        return "0fb0cde6-545c-4172-82b8-d2404dbbfb51";
    }

    @Override
    public List<PluginComponent> pluginComponents() {
        return List.of(
                new PluginComponent(pluginId(), "mediaResourceFetcher", "m-team 馒头", "App", "m-team影视资源")
        );
    }
}
