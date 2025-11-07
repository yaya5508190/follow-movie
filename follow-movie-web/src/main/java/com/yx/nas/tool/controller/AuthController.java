package com.yx.nas.tool.controller;

import com.yx.nas.dto.SysUserInput;
import com.yx.nas.enums.ResponseCodeEmun;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author yx
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public CommonResult<Void> register(@RequestBody SysUserInput request) {
            return userService.createUser(request);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public CommonResult<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            Map<String, Object> data = new HashMap<>();
            data.put("username", authentication.getName());
            data.put("authenticated", true);
            return CommonResult.success(data, "已登录");
        }

        return CommonResult.failure(ResponseCodeEmun.UNAUTHORIZED);
    }
}

