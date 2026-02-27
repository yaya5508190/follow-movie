package com.yx.nas.tool.context;

import com.yx.nas.model.event.BaseSystemEvent;
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

    /**
     * 通用事件监听器 - 处理所有 BaseSystemEvent 子类事件
     * 使用 parentEvent 属性防止重复分发
     */
    @EventListener
    public void onEvent(BaseSystemEvent event) {
        if (!event.getParentEvent()) {
            event.setParentEvent(true);
            log.info("父容器接收到事件,开始广播: {}", getEventDescription(event));
            broadcastToChildContexts(event);
        }
    }

    private void broadcastToChildContexts(BaseSystemEvent event) {
        contextRegistry.getContextMap().values().forEach(context -> {
            log.info("广播到: {}", context.getId());
            context.publishEvent(event);
        });
    }

    private String getEventDescription(BaseSystemEvent event) {
        return event.getClass().getSimpleName();
    }
}
