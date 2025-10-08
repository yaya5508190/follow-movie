package com.yx.nas.tool.plugins.listener;

import com.yx.nas.model.event.DownloadEvent;
import com.yx.nas.tool.plugins.MediaDownloadPluginConfig;
import com.yx.nas.tool.plugins.service.QBittorrentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DownloadEventListener {

    @Resource
    QBittorrentService qBittorrentService;

    @Resource
    private MediaDownloadPluginConfig mediaDownloadPluginConfig;

    @EventListener
    public void onDownloadEvent(DownloadEvent event) {
        // 处理下载事件的逻辑
        log.info("Received download event: {}", event);

        if(mediaDownloadPluginConfig.name().equals(event.getTargetDownloader())){
            if(qBittorrentService.downloadResource(event.getMediaTorrentRecordId())){
                log.info("资源下载任务成功创建，id: {}", event.getMediaTorrentRecordId());
            }else {
                log.error("资源下载任务创建失败，id: {}", event.getMediaTorrentRecordId());
            }
        }
    }
}
