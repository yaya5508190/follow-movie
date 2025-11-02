package com.yx.nas.tool.service;

import com.yx.nas.dto.SysPreAuthView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.SysPreAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysPreAuthSettingService {
    private final SysPreAuthRepository sysPreAuthRepository;

    public CommonResult<List<SysPreAuthView>> queryAllSysPreAuthSettings() {
        List<SysPreAuthView> fetcherSettings = sysPreAuthRepository.queryAllSysPreAuthSettings();
        return CommonResult.success(fetcherSettings);
    }
}
