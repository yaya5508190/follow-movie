package com.yx.nas.tool.service;

import com.yx.nas.dto.SysUserDetailView;
import com.yx.nas.dto.SysUserInput;
import com.yx.nas.dto.SysUserSpec;
import com.yx.nas.dto.SysUserUpdateInput;
import com.yx.nas.dto.SysUserView;
import com.yx.nas.enums.ResponseCodeEmun;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 用户服务
 *
 * @author yx
 * @date 2025-11-05
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 创建用户
     */
    @Transactional
    public CommonResult<Void> createUser(SysUserInput request) {
        // 检查用户名是否已存在
        SysUserDetailView user = sysUserRepository.findByUserName(request.getUserName());
        if (Objects.nonNull(user)) {
            return CommonResult.failure(ResponseCodeEmun.USER_ALREADY_EXISTS);
        }
        request.setEnabled(true);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        sysUserRepository.save(request, SaveMode.INSERT_ONLY);
        return CommonResult.success();
    }

    /**
     * 分页查询用户列表
     */
    public CommonResult<Page<SysUserView>> getUserList(SysUserSpec spec, Pageable pageable) {
        Page<SysUserView> users = sysUserRepository.findUserPage(spec, pageable);
        return CommonResult.success(users);
    }

    /**
     * 查询所有用户
     */
    public CommonResult<List<SysUserView>> getAllUsers() {
        List<SysUserView> users = sysUserRepository.findAllUsers();
        return CommonResult.success(users);
    }

    /**
     * 根据ID查询用户
     */
    public CommonResult<SysUserView> getUserById(Long id) {
        SysUserView user = sysUserRepository.findUserById(id);

        if (user == null) {
            return CommonResult.failure(ResponseCodeEmun.USER_NOT_FOUND);
        }

        return CommonResult.success(user);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public CommonResult<Void> updateUser(SysUserUpdateInput request) {
        // 检查用户是否存在
        if (sysUserRepository.notExistsUserById(request.getId())) {
            return CommonResult.failure(ResponseCodeEmun.USER_NOT_FOUND);
        }

        // 如果提供了新密码，则加密密码
        if (StringUtils.hasText(request.getPassword())) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        sysUserRepository.save(request);
        return CommonResult.success();
    }

    /**
     * 删除用户
     */
    @Transactional
    public CommonResult<Void> deleteUser(Long id) {
        // 检查用户是否存在
        if (sysUserRepository.notExistsUserById(id)) {
            return CommonResult.failure(ResponseCodeEmun.USER_NOT_FOUND);
        }

        sysUserRepository.deleteById(id);
        return CommonResult.success();
    }

    /**
     * 批量删除用户
     */
    @Transactional
    public CommonResult<Void> deleteUsers(List<Long> ids) {
        sysUserRepository.batchDeleteUsers(ids);
        return CommonResult.success();
    }

    /**
     * 启用/禁用用户
     */
    @Transactional
    public CommonResult<Void> toggleUserStatus(Long id, boolean enabled) {
        if (sysUserRepository.notExistsUserById(id)) {
            return CommonResult.failure(ResponseCodeEmun.USER_NOT_FOUND);
        }

        sysUserRepository.updateUserStatus(id, enabled);

        return CommonResult.success();
    }
}

