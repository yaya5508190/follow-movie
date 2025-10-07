package com.yx.nas.tool.context;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Getter
public class ContextRegistry implements ApplicationListener<ContextRefreshedEvent> {
    private final Map<String, ApplicationContext> contextMap = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        // 如果该事件来自某个子容器
        if (ctx.getParent() != null) {
            log.info("✅ 子容器初始化完成: " + ctx.getId());
            contextMap.put(UUID.randomUUID().toString(), ctx);
        }
    }
}
