package com.yx.nas.repository;

import com.yx.nas.dto.SysUserDetailView;
import com.yx.nas.dto.SysUserSpec;
import com.yx.nas.dto.SysUserView;
import com.yx.nas.entity.SysUser;
import com.yx.nas.entity.SysUserTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统用户Repository
 *
 * @author yx
 * @date 2025-11-05
 */
@Repository
public class SysUserRepository extends AbstractJavaRepository<SysUser, Long> {

    SysUserTable table = SysUserTable.$;

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

    /**
     * 分页查询用户列表
     */
    public Page<SysUserView> findUserPage(SysUserSpec spec, Pageable pageable) {
        List<SysUserView> content = sql
                .createQuery(table)
                .where(spec)
                .orderBy(table.id().desc())
                .select(table.fetch(SysUserView.class))
                .limit(pageable.getPageSize(), pageable.getOffset())
                .execute();

        long total = sql
                .createQuery(table)
                .where(spec)
                .select(table.count())
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 查询所有用户
     */
    public List<SysUserView> findAllUsers() {
        return sql
                .createQuery(table)
                .select(table.fetch(SysUserView.class))
                .execute();
    }

    /**
     * 根据ID查询用户
     */
    public SysUserView findUserById(Long id) {
        return sql
                .createQuery(table)
                .where(table.id().eq(id))
                .select(table.fetch(SysUserView.class))
                .fetchOneOrNull();
    }

    /**
     * 检查用户是否存在
     */
    public boolean notExistsUserById(Long id) {
        long count = sql
                .createQuery(table)
                .where(table.id().eq(id))
                .select(table.count()).fetchOne();
        return count == 0;
    }

    /**
     * 批量删除用户
     */
    public void batchDeleteUsers(List<Long> ids) {
        sql.deleteByIds(SysUser.class, ids);
    }

    /**
     * 更新用户状态
     */
    public void updateUserStatus(Long id, boolean enabled) {
        sql.createUpdate(table)
           .set(table.enabled(), enabled)
           .where(table.id().eq(id))
           .execute();
    }
}

