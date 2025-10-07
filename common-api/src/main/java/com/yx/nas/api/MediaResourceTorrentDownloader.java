package com.yx.nas.api;

/**
 * 媒体资源种子下载接口
 */
public interface MediaResourceTorrentDownloader {
    boolean downloadTorrent(String resourceId) throws Exception;
}
