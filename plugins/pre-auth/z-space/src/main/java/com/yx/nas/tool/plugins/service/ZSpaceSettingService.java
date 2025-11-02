package com.yx.nas.tool.plugins.service;

import com.yx.nas.dto.SysPreAuthInput;
import com.yx.nas.dto.SysPreAuthView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.SysPreAuthRepository;
import lombok.AllArgsConstructor;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ZSpaceSettingService {
    private final SysPreAuthRepository sysPreAuthRepository;

    public CommonResult<Void> saveSetting(SysPreAuthInput config) {
        sysPreAuthRepository.save(config.toEntity(), SaveMode.NON_IDEMPOTENT_UPSERT);
        return CommonResult.success();
    }

    public CommonResult<SysPreAuthView> getSetting(Long id) {
        SysPreAuthView config = sysPreAuthRepository.findById(id, SysPreAuthView.class);
        return CommonResult.success(config);
    }

    public CommonResult<Void> deleteSetting(Long id) {
        sysPreAuthRepository.deleteById(id);
        return CommonResult.success();
    }
}
