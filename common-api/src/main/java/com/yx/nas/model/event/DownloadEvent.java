package com.yx.nas.model.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DownloadEvent extends ApplicationEvent {

    public DownloadEvent(Object source, String targetDownloader) {
        super(source);
        this.targetDownloader = targetDownloader;
    }

    /**
     * 目标下载工具
     */
    private String targetDownloader;

    /**
     * 是否是父事件
     */
    private Boolean parentEvent = false;
}
