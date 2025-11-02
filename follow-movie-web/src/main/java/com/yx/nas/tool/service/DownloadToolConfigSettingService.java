package com.yx.nas.tool.service;

import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.DownloadToolConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DownloadToolConfigSettingService {
    private final DownloadToolConfigRepository downloadToolConfigRepository;

    public CommonResult<List<DownloadToolConfigView>> queryAllDownloadToolSettings() {
        List<DownloadToolConfigView> downloadToolSettings = downloadToolConfigRepository.queryAllDownloadToolSettings();
        return CommonResult.success(downloadToolSettings);
    }
}

