package com.yx.nas.repository;

import com.yx.nas.dto.SysPreAuthView;
import com.yx.nas.entity.SysPreAuth;
import com.yx.nas.entity.SysPreAuthTable;
import org.babyfish.jimmer.spring.repo.support.AbstractJavaRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * SysPreAuthRepository 接口
 * </p>
 *
 * @author yx
 * @date 2025-10-22
 */
@Repository
public class SysPreAuthRepository extends AbstractJavaRepository<SysPreAuth, Long> {

    private final SysPreAuthTable table = SysPreAuthTable.$;

    public SysPreAuthRepository(JSqlClient sql) {
        super(sql);
    }

    public List<SysPreAuthView> queryAllSysPreAuthSettings() {
        return sql
                .createQuery(table)
                .select(table.fetch(SysPreAuthView.class))
                .execute();
    }
}

