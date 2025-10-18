package com.yx.nas.tool.controller;

import com.yx.nas.dto.BasicMediaFetchConfig;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.service.MediaResourceFetcherSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mediaResourceFetcherSetting")
@AllArgsConstructor
public class MediaResourceFetcherSettingController {
    private final MediaResourceFetcherSettingService mediaResourceFetcherSettingService;

    //查询所有站点配置
    @PostMapping("/queryAllFetcherSettings")
    public CommonResult<List<BasicMediaFetchConfig>> queryAllFetcherSettings() {
        return mediaResourceFetcherSettingService.queryAllFetcherSettings();
    }
}
