package com.yx.nas.tool.plugins.listener;

import com.yx.nas.model.event.DownloadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DownloadEventListener {
    @EventListener
    public void onDownloadEvent(DownloadEvent event) {
        // 处理下载事件的逻辑
        log.info("Received download event: {}", event.getTargetDownloader());
    }
}
