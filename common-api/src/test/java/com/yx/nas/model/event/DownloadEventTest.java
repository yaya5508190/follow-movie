package com.yx.nas.model.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadEventTest {

    @Test
    @DisplayName("DownloadEvent 继承 BaseSystemEvent")
    void testInheritsFromBaseSystemEvent() {
        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);
        assertThat(event).isInstanceOf(BaseSystemEvent.class);
    }

    @Test
    @DisplayName("构造函数正确设置所有属性")
    void testConstructorSetsAllProperties() {
        Long mediaTorrentRecordId = 123L;
        String targetDownloader = "qbittorrent";
        Long targetDownloaderId = 456L;

        DownloadEvent event = new DownloadEvent(this, mediaTorrentRecordId, targetDownloader, targetDownloaderId);

        assertThat(event.getMediaTorrentRecordId()).isEqualTo(mediaTorrentRecordId);
        assertThat(event.getTargetDownloader()).isEqualTo(targetDownloader);
        assertThat(event.getTargetDownloaderId()).isEqualTo(targetDownloaderId);
        assertThat(event.getParentEvent()).isFalse(); // 继承自 BaseSystemEvent
    }

    @Test
    @DisplayName("parentEvent 可以通过 setter 修改")
    void testParentEventCanBeModified() {
        DownloadEvent event = new DownloadEvent(this, 1L, "qbittorrent", 100L);
        assertThat(event.getParentEvent()).isFalse();

        event.setParentEvent(true);
        assertThat(event.getParentEvent()).isTrue();
    }
}
