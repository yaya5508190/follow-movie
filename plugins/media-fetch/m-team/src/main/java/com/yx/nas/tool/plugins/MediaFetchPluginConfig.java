package com.yx.nas.tool.plugins;

import api.PluginComponent;
import api.PluginConfig;

import java.util.List;
import java.util.Map;

public class MediaFetchPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "";
    }

    @Override
    public String version() {
        return "";
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
        return List.of();
    }
}
