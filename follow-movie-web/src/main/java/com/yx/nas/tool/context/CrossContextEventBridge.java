package com.yx.nas.tool.context;

import com.yx.nas.model.event.DownloadEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrossContextEventBridge {

    private final ContextRegistry contextRegistry;

    public CrossContextEventBridge(ContextRegistry contextRegistry) {
        this.contextRegistry = contextRegistry;
    }

    @EventListener
    public void onDownloadEvent(DownloadEvent event) {
        // 处理下载事件的逻辑
        if(!event.getParentEvent()) {
            event.setParentEvent(true);
            log.info("父容器接收到事件,开始分发: {}", event.getTargetDownloader());
            contextRegistry.getContextMap().values().forEach(context -> {
                log.info("分发到: {}", context.getId());
                context.publishEvent(event);
            });
        }
    }
}
