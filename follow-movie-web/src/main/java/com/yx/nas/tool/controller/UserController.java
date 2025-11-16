package com.yx.nas.tool.controller;

import com.yx.nas.dto.SysUserInput;
import com.yx.nas.dto.SysUserSpec;
import com.yx.nas.dto.SysUserUpdateInput;
import com.yx.nas.dto.SysUserView;
import com.yx.nas.enums.ResponseCodeEmun;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.tool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author yx
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

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

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public CommonResult<Page<SysUserView>> getUserList(
            SysUserSpec spec,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getUserList(spec, pageable);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/all")
    public CommonResult<List<SysUserView>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public CommonResult<SysUserView> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public CommonResult<Void> createUser(@RequestBody SysUserInput request) {
        return userService.createUser(request);
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    public CommonResult<Void> updateUser(@RequestBody SysUserUpdateInput request) {
        return userService.updateUser(request);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    public CommonResult<Void> batchDeleteUsers(@RequestBody List<Long> ids) {
        return userService.deleteUsers(ids);
    }

    /**
     * 启用/禁用用户
     */
    @PatchMapping("/{id}/status")
    public CommonResult<Void> toggleUserStatus(@PathVariable("id") Long id, @RequestParam("enabled") boolean enabled) {
        return userService.toggleUserStatus(id, enabled);
    }
}

