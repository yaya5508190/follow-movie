package com.yx.nas.tool.plugins.controller;

import com.yx.nas.dto.SysPreAuthView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.plugins.service.SysPreAuthSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/preAuthSetting")
@AllArgsConstructor
public class SysPreAuthSettingController {
    private final SysPreAuthSettingService sysPreAuthSettingService;

    //查询所有前置认证配置
    @PostMapping("/queryAllPreAuthSettings")
    public CommonResult<List<SysPreAuthView>> queryAllFetcherSettings() {
        return sysPreAuthSettingService.queryAllSysPreAuthSettings();
    }
}
