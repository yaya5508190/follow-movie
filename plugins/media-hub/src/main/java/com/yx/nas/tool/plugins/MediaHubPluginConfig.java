package com.yx.nas.tool.plugins;

import api.PluginConfig;
import com.google.auto.service.AutoService;

@AutoService(PluginConfig.class)
public class MediaHubPluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "影片推荐";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public String desc() {
        return "影片推荐,支持数据源横向扩展";
    }

    @Override
    public String pluginId() {
        return "9d220be1-8f62-4819-8f00-03911dc00a95";
    }
}
