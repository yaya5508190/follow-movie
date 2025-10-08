package com.yx.nas.model.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class DownloadEvent extends ApplicationEvent {

    public DownloadEvent(Object source,Long mediaTorrentRecordId, String targetDownloader) {
        super(source);
        this.targetDownloader = targetDownloader;
        this.mediaTorrentRecordId = mediaTorrentRecordId;
    }

    /**
     * 目标下载工具
     */
    private String targetDownloader;

    /**
     * 种子ID
     */
    private Long mediaTorrentRecordId;

    /**
     * 是否是父事件
     */
    private Boolean parentEvent = false;
}
