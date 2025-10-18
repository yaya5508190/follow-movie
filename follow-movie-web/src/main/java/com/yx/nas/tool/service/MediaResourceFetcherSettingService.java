package com.yx.nas.tool.service;

import com.yx.nas.dto.BasicMediaFetchConfig;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.MediaFetchConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MediaResourceFetcherSettingService {
    public final MediaFetchConfigRepository mediaFetchConfigRepository;

    public CommonResult<List<BasicMediaFetchConfig>> queryAllFetcherSettings(){
        List<BasicMediaFetchConfig> fetcherSettings = mediaFetchConfigRepository.queryAllFetcherSettings();
        return CommonResult.success(fetcherSettings);
    }
}
