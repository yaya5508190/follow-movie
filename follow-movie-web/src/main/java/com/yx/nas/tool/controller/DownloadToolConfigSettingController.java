package com.yx.nas.tool.controller;

import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.service.DownloadToolConfigSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/downloadToolSetting")
@AllArgsConstructor
public class DownloadToolConfigSettingController {
    private final DownloadToolConfigSettingService downloadToolConfigSettingService;

    //查询所有下载工具配置
    @PostMapping("/queryAllDownloadToolSettings")
    public CommonResult<List<DownloadToolConfigView>> queryAllDownloadToolSettings() {
        return downloadToolConfigSettingService.queryAllDownloadToolSettings();
    }
}

