package com.yx.nas.tool.plugins.controller;

import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.api.MediaResourceTorrentDownloader;
import com.yx.nas.dto.ApiKeyMediaFetchConfigInput;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import com.yx.nas.tool.plugins.service.impl.MediaResourceFetcherSettingImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mediaResource")
public class MTeamMediaResourceFetcherController {

    private final MediaResourceFetcher mediaResourceFetcher;
    private final MediaResourceTorrentDownloader mediaResourceTorrentDownloader;
    private final MediaResourceFetcherSettingImpl mediaResourceFetcherSetting;

    public MTeamMediaResourceFetcherController(
            MediaResourceFetcher mediaResourceFetcher,
            MediaResourceTorrentDownloader mediaResourceTorrentDownloader,
            MediaResourceFetcherSettingImpl mediaResourceFetcherSetting) {
        this.mediaResourceFetcher = mediaResourceFetcher;
        this.mediaResourceTorrentDownloader = mediaResourceTorrentDownloader;
        this.mediaResourceFetcherSetting = mediaResourceFetcherSetting;
    }

    @PostMapping("/search")
    public PageResult<MediaResourceDto> search(@RequestBody MediaResourcePageReqVo reqVo) throws Exception {
        return mediaResourceFetcher.search(reqVo);
    }

    @PostMapping("/downloadTorrent/{resourceId}")
    public boolean downloadTorrent(@PathVariable("resourceId") String resourceId) throws Exception {
        return mediaResourceTorrentDownloader.downloadTorrent(resourceId);
    }

    @PostMapping("/saveSetting")
    public CommonResult<Void> saveSetting(@RequestBody ApiKeyMediaFetchConfigInput config) {
        return mediaResourceFetcherSetting.saveSetting(config);
    }

    @PostMapping("/getSetting/{id}")
    public CommonResult<ApiKeyMediaFetchConfigInput> getSetting(@PathVariable("id") Long id) {
        return mediaResourceFetcherSetting.getSetting(id);
    }
}
