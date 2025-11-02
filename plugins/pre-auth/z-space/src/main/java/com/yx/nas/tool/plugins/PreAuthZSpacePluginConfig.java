package com.yx.nas.tool.plugins;

import api.PluginComponent;
import api.PluginConfig;
import com.google.auto.service.AutoService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@AutoService(PluginConfig.class)
@Configuration
@ComponentScan(basePackages = "com.yx.framework")
public class PreAuthZSpacePluginConfig implements PluginConfig {
    @Override
    public String name() {
        return "极空间远程访问认证";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public String desc() {
        return "极空间远程访问认证";
    }

    @Override
    public String pluginId() {
        return "67c6ec24-a9a7-430e-ae2f-321ab5fe7589";
    }

    @Override
    public List<PluginComponent> pluginComponents() {
        return List.of(
                new PluginComponent(pluginId(), "preAuthSetting", "极空间", "App-Setting", "极空间NAS集成设置")
        );
    }
}
