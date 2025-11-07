package com.yx.nas.repository;

import com.yx.nas.dto.SysUserDetailView;
import com.yx.nas.entity.SysUser;
import com.yx.nas.entity.SysUserTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

/**
 * 系统用户Repository
 *
 * @author yx
 * @date 2025-11-05
 */
@Repository
public class SysUserRepository extends AbstractJavaRepository<SysUser, Long> {
    private final SysUserTable table = SysUserTable.$;

    public SysUserRepository(JSqlClient sql) {
        super(sql);
    }

    /**
     * 根据用户名查找用户
     *
     * @param userName 用户名
     * @return 用户实体
     */
    public SysUserDetailView findByUserName(String userName) {
        return sql
                .createQuery(table)
                .where(
                        table.userName().eq(userName),
                        table.enabled().eq(true)
                )
                .select(table.fetch(SysUserDetailView.class))
                .fetchFirstOrNull();
    }
}

