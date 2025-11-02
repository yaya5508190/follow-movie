package com.yx.nas.tool.plugins.controller;

import com.yx.nas.dto.DownloadToolConfigInput;
import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.plugins.service.DownloadToolConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/q-bittorrent")
@AllArgsConstructor
public class QBittorrentController {
    private final DownloadToolConfigService downloadToolConfigService;

    @PostMapping("/saveSetting")
    public CommonResult<Void> saveSetting(@RequestBody DownloadToolConfigInput config) {
        return downloadToolConfigService.saveSetting(config);
    }

    @PostMapping("/getSetting/{id}")
    public CommonResult<DownloadToolConfigView> getSetting(@PathVariable("id") Long id) {
        return downloadToolConfigService.getSetting(id);
    }

    @PostMapping("/deleteSetting/{id}")
    public CommonResult<Void> deleteSetting(@PathVariable("id") Long id) {
        return downloadToolConfigService.deleteSetting(id);
    }
}

