package com.yx.nas.tool.service;

import com.yx.nas.dto.SysUserDetailView;
import com.yx.nas.dto.SysUserInput;
import com.yx.nas.enums.ResponseCodeEmun;
import com.yx.nas.model.common.CommonResult;
import com.yx.nas.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

