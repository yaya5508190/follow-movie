package com.yx.nas.tool.plugins.controller;

import com.yx.nas.dto.SysPreAuthInput;
import com.yx.nas.dto.SysPreAuthView;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.plugins.service.ZSpaceSettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/z-space")
@AllArgsConstructor
public class ZSpaceSettingController {
    private final ZSpaceSettingService zSpaceSettingService;

    @PostMapping("/saveSetting")
    public CommonResult<Void> saveSetting(@RequestBody SysPreAuthInput config) {
        return zSpaceSettingService.saveSetting(config);
    }

    @PostMapping("/getSetting/{id}")
    public CommonResult<SysPreAuthView> getSetting(@PathVariable("id") Long id) {
        return zSpaceSettingService.getSetting(id);
    }
}
