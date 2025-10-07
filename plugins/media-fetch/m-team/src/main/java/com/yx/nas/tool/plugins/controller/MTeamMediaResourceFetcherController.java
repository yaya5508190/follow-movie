package com.yx.nas.tool.plugins.controller;

import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.api.MediaResourceTorrentDownloader;
import com.yx.nas.dto.MediaTorrentRecordInput;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mediaResource")
public class MTeamMediaResourceFetcherController {

    private final MediaResourceFetcher mediaResourceFetcher;
    private final MediaResourceTorrentDownloader mediaResourceTorrentDownloader;

    public MTeamMediaResourceFetcherController(
            MediaResourceFetcher mediaResourceFetcher,
            MediaResourceTorrentDownloader mediaResourceTorrentDownloader) {
        this.mediaResourceFetcher = mediaResourceFetcher;
        this.mediaResourceTorrentDownloader = mediaResourceTorrentDownloader;
    }

    @PostMapping("/search")
    public PageResult<MediaResourceDto> search(@RequestBody MediaResourcePageReqVo reqVo) throws Exception {
        return mediaResourceFetcher.search(reqVo);
    }

    @PostMapping("/downloadTorrent/{resourceId}")
    public boolean downloadTorrent(@PathVariable("resourceId") String resourceId) throws Exception {
        return mediaResourceTorrentDownloader.downloadTorrent(resourceId);
    }
}
