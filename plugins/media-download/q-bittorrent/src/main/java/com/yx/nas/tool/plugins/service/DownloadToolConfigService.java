package com.yx.nas.tool.plugins.service;

import com.yx.nas.dto.DownloadToolConfigInput;
import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.DownloadToolConfigRepository;
import lombok.AllArgsConstructor;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DownloadToolConfigService {
    private final DownloadToolConfigRepository downloadToolConfigRepository;

    @Transactional
    public CommonResult<Void> saveSetting(DownloadToolConfigInput config) {
        // 直接保存，Jimmer 会根据 DTO 中的 mediaFetchConfigIds 自动处理关联
        downloadToolConfigRepository.save(config.toEntity(), SaveMode.NON_IDEMPOTENT_UPSERT);
        return CommonResult.success();
    }

    public CommonResult<DownloadToolConfigView> getSetting(Long id) {
        DownloadToolConfigView config = downloadToolConfigRepository.findById(id, DownloadToolConfigView.class);
        return CommonResult.success(config);
    }

    @Transactional
    public CommonResult<Void> deleteSetting(Long id) {
        downloadToolConfigRepository.deleteById(id);
        return CommonResult.success();
    }
}

